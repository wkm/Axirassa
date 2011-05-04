
package axirassa.webapp.ajax

import org.apache.tapestry5.json.JSONObject
import org.cometd.bayeux.server.BayeuxServer
import org.cometd.server.AbstractService
import org.hornetq.api.core.HornetQException
import axirassa.services.InjectorService
import axirassa.util.MessagingTools
import axirassa.util.MessagingTopic

class PingerStreamingService(server : BayeuxServer) extends AbstractService(server, "pingerService", 5) {

  spawnPingerService()

  private def spawnPingerService() {
    val thread = new Thread(new Runnable() {
      override def run() {
        try {
          pingerService()
        } catch {
          case e : HornetQException => e.printStackTrace()
        }
      }
    })

    thread.start()
  }

  private def pingerService() {
    val messagingSession = MessagingTools.getEmbeddedSession()
    val topic = new MessagingTopic(messagingSession, "ax.account.#")
    val consumer = topic.createConsumer

    messagingSession.start()
    // System.out.println("Starting pinger service")

    while (true) {
      try {
        val message = consumer.receive()
        val stat = InjectorService.rebuildMessage(message)
        if (stat != null) {
          println("RECEIVED MESSAGE: "+stat)

          val pinger = stat.getPinger()
          val channel = getLocalSession().getChannel("/ax/pinger/"+pinger.getId())
          val jsonMessage = new JSONObject()

          jsonMessage.put("Date", stat.getTimestamp().toString())
          jsonMessage.put("StatusCode", stat.getStatusCode())
          jsonMessage.put("Latency", stat.getLatency())
          jsonMessage.put("TransferTime", stat.getResponseTime())
          jsonMessage.put("ResponseSize", stat.getResponseSize())

          channel.publish(jsonMessage.toCompactString())
          println("\t >>>> [ published ]")
        }
      } catch {
        case e : Exception => e.printStackTrace()
      }
    }
  }
}
