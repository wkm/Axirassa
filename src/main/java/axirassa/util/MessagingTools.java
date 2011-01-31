
package axirassa.util;

import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.core.client.ClientSession;
import org.hornetq.api.core.client.ClientSessionFactory;
import org.hornetq.api.core.client.HornetQClient;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;

public class MessagingTools {
	public static ClientSession getEmbeddedSession() throws HornetQException {
		ClientSessionFactory factory = HornetQClient.createClientSessionFactory(new TransportConfiguration(
		        NettyConnectorFactory.class.getName()));

		ClientSession session = factory.createSession();

		return session;
	}
}
