
package axirassa.webapp.ajax

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.HashSet
import java.util.Random

import org.cometd.bayeux.client.ClientSessionChannel
import org.cometd.bayeux.server.BayeuxServer
import org.cometd.bayeux.server.ConfigurableServerChannel
import org.cometd.bayeux.server.ServerChannel
import org.cometd.bayeux.server.ServerSession
import org.cometd.server.AbstractService

class TimeService(server : BayeuxServer) extends AbstractService(server, "timeService") {

  var channel : ServerChannel = _
  var subscribers = new HashSet[ServerSession]

  val initalizer = new ConfigurableServerChannel.Initializer() {
    override def configureChannel(channel : ConfigurableServerChannel) {
      println("Configuring channel")
      channel.setPersistent(true)
    }
  }

  println("####### INITIALIZING TIME SERVICE")
  startTimeFilter()

  private def startTimeFilter() {
    System.err.println("FORKING TIME FILTER THREAD")
    val channel1 = getLocalSession().getChannel("/ax/timeplease")
    val channel2 = getLocalSession().getChannel("/ax/valueplease")
    // final ClientSessionChannel channel3 =
    // getLocalSession().getChannel("/ax/pinger/6351")
    val format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SS z")
    val thread : Thread = new Thread(new Runnable() {
      override def run() {
        while (true) {
          channel1.publish(format.format(new Date()))
          channel2.publish(new Random().nextDouble())
          // channel3.publish(new Random().nextDouble())

          Thread.sleep(5000)

        }
      }
    })

    thread.start()
  }
}
