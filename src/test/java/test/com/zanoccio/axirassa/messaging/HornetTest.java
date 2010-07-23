
package test.com.zanoccio.axirassa.messaging;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.jms.HornetQJMSClient;
import org.hornetq.core.remoting.impl.invm.InVMConnectorFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import com.zanoccio.axirassa.util.EmbeddedMessagingServer;

public class HornetTest {
	@BeforeClass()
	public static void startHornet() throws Exception {
		EmbeddedMessagingServer.start();
	}


	@Test
	public void test() throws JMSException {
		ConnectionFactory cf = HornetQJMSClient.createConnectionFactory(new TransportConfiguration(
		        InVMConnectorFactory.class.getName()));

		Connection conn = cf.createConnection();
		conn.start();

		Session sess = conn.createSession(true, Session.SESSION_TRANSACTED);
		Queue queue = sess.createQueue("Pinger");
		MessageProducer prod = sess.createProducer(queue);

		TextMessage msg = sess.createTextMessage("Hello!");
		prod.send(msg);

		sess.commit();

		MessageConsumer consumer = sess.createConsumer(queue);
		TextMessage txtmsg = (TextMessage) consumer.receive();

		System.out.println("Msg = " + txtmsg.getText());

		sess.commit();
		conn.close();
	}

}
