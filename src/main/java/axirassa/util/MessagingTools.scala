
package axirassa.util

import org.hornetq.api.core.HornetQException
import org.hornetq.api.core.TransportConfiguration
import org.hornetq.api.core.client.ClientSession
import org.hornetq.api.core.client.ClientSessionFactory
import org.hornetq.api.core.client.HornetQClient
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory

object MessagingTools {
  def getEmbeddedSession() = {
    val factory = HornetQClient.createClientSessionFactory(
      new TransportConfiguration(classOf[NettyConnectorFactory].getName()))
    factory.createSession()
  }
}
