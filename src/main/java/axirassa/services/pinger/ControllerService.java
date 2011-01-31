
package axirassa.services.pinger;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientProducer;
import org.hornetq.api.core.client.ClientSession;

import axirassa.config.Messaging;
import axirassa.messaging.PingerRequestMessage;
import axirassa.model.PingerModel;
import axirassa.services.Service;
import axirassa.util.AutoSerializingObject;
import axirassa.util.EmbeddedMessagingServer;
import axirassa.util.HibernateTools;
import axirassa.util.MessagingTools;
import axirassa.util.Meta;

/**
 * The ControllerService executes every minute, creating a message for each
 * triggered pinger.
 * 
 * @author wiktor
 */
public class ControllerService implements Service {

	private final ClientSession messagingSession;
	private final Session databaseSession;


	public ControllerService(ClientSession messagingSession, Session databaseSession) {
		this.messagingSession = messagingSession;
		this.databaseSession = databaseSession;
	}


	@Override
	public void execute() throws Exception {
		ClientProducer producer = messagingSession.createProducer(Messaging.PINGER_REQUEST_QUEUE);

		Query query = databaseSession.getNamedQuery("pingers_by_frequency");
		query.setInteger("frequency", 3600);

		List<PingerModel> pingers = query.list();
		for (PingerModel pinger : pingers) {
			ClientMessage message = messagingSession.createMessage(false);
			message.getBodyBuffer().writeBytes(new PingerRequestMessage(pinger).toBytes());
			producer.send(message);
		}

		producer.close();

		// we have to start before reading messages
		messagingSession.start();

		ClientConsumer consumer = messagingSession.createConsumer(Messaging.PINGER_REQUEST_QUEUE);
		while (true) {
			ClientMessage message = consumer.receiveImmediate();
			if (message == null) {
				System.out.println("No more messages.");
				break;
			}

			byte[] buffer = new byte[message.getBodyBuffer().readableBytes()];
			message.getBodyBuffer().readBytes(buffer);

			Object rawobject = AutoSerializingObject.fromBytes(buffer);
			if (rawobject instanceof PingerRequestMessage)
				Meta.inspect(rawobject);
			else
				System.err.println("WTF IS THIS: " + rawobject);
		}

		messagingSession.stop();
	}


	public static void main(String[] args) throws Exception {
		EmbeddedMessagingServer.start();

		ClientSession msgsession = MessagingTools.getEmbeddedSession();
		Session dbsession = HibernateTools.getSession();

		Service service = new ControllerService(msgsession, dbsession);

		System.out.println("Executing injector");
		service.execute();
		System.out.println("Finished executing");

		return;
	}
}
