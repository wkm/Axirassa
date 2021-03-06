
package test.axirassa.messaging;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;

import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientProducer;
import org.hornetq.api.core.client.ClientSession;
import org.hornetq.api.core.client.ClientSessionFactory;
import org.hornetq.api.core.client.HornetQClient;
import org.hornetq.api.core.client.ServerLocator;
import org.hornetq.core.remoting.impl.netty.NettyConnector;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import axirassa.util.EmbeddedMessagingServer;
import axirassa.util.MessagingTopic;

public class TestMessageTopics {

	@BeforeClass
	public static void startServer() throws Exception {
		EmbeddedMessagingServer.start();
	}


	@AfterClass
	public static void stopServer() throws Exception {
		EmbeddedMessagingServer.stop();
	}


	private ClientMessage textMessage(ClientSession session, String txt) {
		ClientMessage msg = session.createMessage(false);
		msg.getBodyBuffer().writeString(txt);
		return msg;
	}


	private String getMessageText(ClientMessage msg) {
		if (msg == null)
			return null;
		System.out.println("Disassembling message: " + msg + " buff: " + msg.getBodyBuffer().capacity());
		return msg.getBodyBuffer().readString();
	}


	@Test
	@Ignore
	public void test() throws Exception {
		TransportConfiguration config = new TransportConfiguration(NettyConnector.class.getName());
		ServerLocator locator = HornetQClient.createServerLocatorWithoutHA(config);
		ClientSessionFactory factory = locator.createSessionFactory();
		ClientSession session = factory.createSession();

		String topic1 = "ax.account.1.pinger.12";
		String topic2 = "ax.account.1.pinger.13";
		String topic3 = "ax.account.7.pinger.38";

		// speed test for messages effectively sent to the trash
		ClientProducer producer = session.createProducer();
		speedTest(session, producer, topic1);

		session.start();
		ClientConsumer topic1Consumer1 = new MessagingTopic(session, topic1).createConsumer();
		ClientConsumer topic1Consumer2 = new MessagingTopic(session, topic1).createConsumer();

		// make sure there are no messages on the queue before anyone subscribed
		assertNull(topic1Consumer1.receiveImmediate());
		assertNull(topic1Consumer2.receiveImmediate());

		// post a message
		producer.send(topic1, textMessage(session, "hello topic1 again"));

		// verify that *both* consumers received the message
		assertEquals("hello topic1 again", getMessageText(topic1Consumer1.receiveImmediate()));
		assertEquals("hello topic1 again", getMessageText(topic1Consumer2.receiveImmediate()));

		ClientConsumer topic2Consumer1 = new MessagingTopic(session, topic2).createConsumer();
		ClientConsumer topic3Consumer1 = new MessagingTopic(session, topic3).createConsumer();

		// post a message to topic2
		producer.send(topic2, textMessage(session, "topic2"));

		assertEquals("topic2", getMessageText(topic2Consumer1.receiveImmediate()));
		assertNull(topic1Consumer1.receiveImmediate());
		assertNull(topic1Consumer2.receiveImmediate());
		assertNull(topic3Consumer1.receiveImmediate());

		ClientConsumer account1Consumer = new MessagingTopic(session, "ax.account.1.pinger.*").createConsumer();

		// post a message to topic1 and then topic2
		producer.send(topic1, textMessage(session, "msg1"));
		producer.send(topic2, textMessage(session, "msg2"));

		// both topic 1 subscribers got the message
		assertEquals("msg1", getMessageText(topic1Consumer1.receiveImmediate()));
		assertEquals("msg1", getMessageText(topic1Consumer2.receiveImmediate()));

		// and so did the account subscriber
		assertEquals("msg1", getMessageText(account1Consumer.receiveImmediate()));

		assertEquals("msg2", getMessageText(topic2Consumer1.receiveImmediate()));
		assertEquals("msg2", getMessageText(account1Consumer.receiveImmediate()));

		// test message sending... heh heh
		int consumerCount = 10000;
		int messageCount = 100;
		long start = System.currentTimeMillis();
		ArrayList<ClientConsumer> freakshow = new ArrayList<ClientConsumer>(10000);
		for (int i = 0; i < consumerCount; i++)
			freakshow.add(new MessagingTopic(session, topic3).createConsumer());
		System.err.println("Created 10k consumers in: " + (System.currentTimeMillis() - start));

		start = System.currentTimeMillis();
		for (int i = 0; i < messageCount; i++)
			producer.send(topic3, textMessage(session, "hi... you broke yet?"));
		System.err.println("Sent 100 messages to 10k consumers in: " + (System.currentTimeMillis() - start));

		// c c c consume those babies
		start = System.currentTimeMillis();
		for (int i = 0; i < messageCount; i++)
			for (ClientConsumer consumer : freakshow)
				assertNotNull(consumer.receiveImmediate());

		System.err.println("Consumed 100 messages through 10000 consumers in: " + (System.currentTimeMillis() - start));

		session.stop();
	}


	public void speedTest(ClientSession session, ClientProducer producer, String topic) throws HornetQException {
		long start = System.currentTimeMillis();
		for (int i = 0; i < 50000; i++)
			producer.send(topic, textMessage(session, "hello topic1"));
		System.err.println("Finit after: " + (System.currentTimeMillis() - start));
	}


	@AfterClass
	public static void shutdownServer() throws Exception {
		EmbeddedMessagingServer.stop();
	}
}
