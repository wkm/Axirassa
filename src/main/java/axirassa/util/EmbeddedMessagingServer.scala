
package axirassa.util

import java.util.HashSet
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

import org.hornetq.api.core.TransportConfiguration
import org.hornetq.core.config.impl.FileConfiguration
import org.hornetq.core.remoting.impl.invm.InVMAcceptorFactory
import org.hornetq.core.remoting.impl.netty.NettyAcceptorFactory
import org.hornetq.core.server.HornetQServer
import org.hornetq.core.server.HornetQServers
import org.hornetq.core.server.JournalType

/**
 * Starts HornetQ as an embedded messaging server.
 *
 * @author wiktor
 */
object EmbeddedMessagingServer {
  var server : HornetQServer = null
  def start() {
    val config = new FileConfiguration()

    config.setConfigurationUrl("hornetq-configuration.xml")

    val transports = new HashSet[TransportConfiguration]
    transports.add(new TransportConfiguration(classOf[NettyAcceptorFactory].getName()))
    transports.add(new TransportConfiguration(classOf[InVMAcceptorFactory].getName()))

    config.setJournalType(JournalType.NIO)
    config.setAcceptorConfigurations(transports)
    config.setSecurityEnabled(false)

    config.start()

    server = HornetQServers.newHornetQServer(config)
    server.start()

    println("Axirassa Embedded HornetQ server started.")

    val lister = new ServerQueueLister(server)
    val executor = new ScheduledThreadPoolExecutor(1)
    executor.scheduleAtFixedRate(lister, 1, 1, TimeUnit.MINUTES)
  }

  def stop() {
    server.stop()
  }

  def main(args : Array[String]) {
    start()
  }
}

class ServerQueueLister(val server : HornetQServer) extends Runnable {
  override def run() {
    val queues = server.getHornetQServerControl().getQueueNames()
    println("QUEUES:")
    for (queue <- queues)
      println("\t"+queue)
  }
}
