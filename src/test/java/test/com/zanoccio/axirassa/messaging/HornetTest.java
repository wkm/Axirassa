
package test.com.zanoccio.axirassa.messaging;

import javax.jms.JMSException;

import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientProducer;
import org.hornetq.api.core.client.ClientSession;
import org.hornetq.api.core.client.ClientSessionFactory;
import org.hornetq.api.core.client.HornetQClient;
import org.hornetq.core.remoting.impl.invm.InVMConnectorFactory;
import org.junit.Test;

import com.zanoccio.axirassa.util.EmbeddedMessagingServer;

public class HornetTest {
	static {
		try {
			EmbeddedMessagingServer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Test
	public void test() throws JMSException, HornetQException {
		ClientSessionFactory factory = HornetQClient.createClientSessionFactory(new TransportConfiguration(
		        InVMConnectorFactory.class.getName()));

		ClientSession session = factory.createSession();

		session.createQueue("example", "example", true);

		ClientProducer producer = session.createProducer("example");

		ClientMessage message = session.createMessage(true);

		message.getBodyBuffer().writeString("Hello");

		producer.send(message);
		producer.close();

		session.start();

		ClientConsumer consumer = session.createConsumer("example");

		ClientMessage msgReceived = consumer.receive();

		System.out.println("message = " + msgReceived.getBodyBuffer().readString());

		consumer.close();

		session.deleteQueue("example");
		session.close();
	}

}
