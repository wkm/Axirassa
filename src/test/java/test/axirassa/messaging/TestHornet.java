
package test.axirassa.messaging;

import static org.junit.Assert.assertEquals;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientProducer;
import org.hornetq.api.core.client.ClientSession;
import org.hornetq.api.core.client.ClientSessionFactory;
import org.hornetq.api.core.client.HornetQClient;
import org.hornetq.api.core.client.ServerLocator;
import org.hornetq.core.remoting.impl.netty.NettyAcceptorFactory;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import axirassa.util.EmbeddedMessagingServer;

public class TestHornet {
	@BeforeClass
	public static void startServer() throws Exception {
		EmbeddedMessagingServer.start();
	}


	@AfterClass
	public static void stopServer() throws Exception {
		EmbeddedMessagingServer.stop();
	}


	@Test
	public void test() throws Exception {
		TransportConfiguration config = new TransportConfiguration(NettyConnectorFactory.class.getName());
		ServerLocator locator = HornetQClient.createServerLocatorWithoutHA(config);
		ClientSessionFactory factory = locator.createSessionFactory();
		ClientSession session = factory.createSession();

		session.createQueue("example", "example", false);

		ClientProducer producer = session.createProducer("example");
		ClientMessage message = session.createMessage(false);

		message.getBodyBuffer().writeString("Hello");

		producer.send(message);
		producer.close();

		session.start();

		ClientConsumer consumer = session.createConsumer("example");
		ClientMessage msgReceived = consumer.receive();

		String msg = msgReceived.getBodyBuffer().readString();
		assertEquals("Hello", msg);

		System.out.println("HornetQ message body: " + msg);

		consumer.close();

		session.deleteQueue("example");
		session.close();
	}

}
