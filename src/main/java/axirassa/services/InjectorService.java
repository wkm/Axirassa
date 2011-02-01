
package axirassa.services;

import java.util.ArrayList;

import org.hibernate.Session;
import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientSession;

import axirassa.config.Messaging;
import axirassa.messaging.PingerResponseMessage;
import axirassa.model.HttpStatisticsEntity;
import axirassa.services.exceptions.InvalidMessageClassException;
import axirassa.util.AutoSerializingObject;

/**
 * The InjectorService relatively frequently. It is designed to insert responses
 * on the high-speed message queue into the database in batches.
 * 
 * @author wiktor
 */
public class InjectorService implements Service {

	private final ClientSession messagingSession;
	private final Session databaseSession;


	public InjectorService(ClientSession messagingSession, Session databaseSession) {
		this.messagingSession = messagingSession;
		this.databaseSession = databaseSession;
	}


	@Override
	public void execute() throws Exception {
		ClientConsumer consumer = messagingSession.createConsumer(Messaging.PINGER_RESPONSE_QUEUE);
		messagingSession.start();

		ArrayList<HttpStatisticsEntity> entities = new ArrayList<HttpStatisticsEntity>();

		while (true) {
			ClientMessage message = consumer.receiveImmediate();
			if (message == null) {
				System.out.println("NO MORE INJECTOR REQUESTS");
				break;
			}

			byte[] buffer = new byte[message.getBodyBuffer().readableBytes()];
			message.getBodyBuffer().readBytes(buffer);

			Object rawobject = AutoSerializingObject.fromBytes(buffer);
			if (rawobject instanceof PingerResponseMessage) {
				PingerResponseMessage response = (PingerResponseMessage) rawobject;

				entities.add(response.createEntity());

			} else
				throw new InvalidMessageClassException(PingerResponseMessage.class, rawobject);
		}

		System.out.println("READY FOR INJECTION: " + entities);

		messagingSession.stop();
	}
}
