
package axirassa.services;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientProducer;
import org.hornetq.api.core.client.ClientSession;

import axirassa.config.Messaging;
import axirassa.messaging.PingerRequestMessage;
import axirassa.model.PingerEntity;

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

		List<PingerEntity> pingers = query.list();
		for (PingerEntity pinger : pingers) {
			ClientMessage message = messagingSession.createMessage(false);
			message.getBodyBuffer().writeBytes(new PingerRequestMessage(pinger).toBytes());
			producer.send(message);
		}

		System.out.println("POPULATED " + pingers.size() + " PINGERS");

		producer.close();
	}
}
