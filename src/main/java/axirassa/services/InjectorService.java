
package axirassa.services;

import java.io.IOException;
import java.util.ArrayList;

import org.hibernate.Session;
import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientSession;

import axirassa.config.Messaging;
import axirassa.model.HttpStatisticsEntity;
import axirassa.services.exceptions.InvalidMessageClassException;
import axirassa.util.MessagingTools;

/**
 * The InjectorService relatively frequently. It is designed to insert responses
 * on the high-speed message queue into the database in batches.
 * 
 * @author wiktor
 */
public class InjectorService implements Service {

	public static HttpStatisticsEntity rebuildMessage (ClientMessage message) throws HornetQException, IOException,
	        ClassNotFoundException, InvalidMessageClassException {
		if (message == null)
			return null;

		HttpStatisticsEntity statistic = MessagingTools.fromMessageBytes(HttpStatisticsEntity.class, message);
		message.acknowledge();
		return statistic;
	}


	private final ClientSession messagingSession;
	private final Session databaseSession;

	private static final int FLUSH_SIZE = 1000;


	public InjectorService (ClientSession messagingSession, Session databaseSession) {
		this.messagingSession = messagingSession;
		this.databaseSession = databaseSession;
	}


	@Override
	public void execute () throws Exception {
		ClientConsumer consumer = messagingSession.createConsumer(Messaging.PINGER_RESPONSE_QUEUE);
		messagingSession.start();

		ArrayList<HttpStatisticsEntity> entities = new ArrayList<HttpStatisticsEntity>();

		while (true) {
			ClientMessage message = consumer.receiveImmediate();
			if (message == null)
				break;

			entities.add(rebuildMessage(message));
		}

		consumer.close();
		messagingSession.stop();

		databaseSession.beginTransaction();
		int entityCounter = 0;
		for (HttpStatisticsEntity entity : entities) {
			databaseSession.save(entity);
			entityCounter++;

			// it's necessary to flush the session frequently to prevent the
			// memory-cache of entities to use up the heap
			if ((entityCounter % FLUSH_SIZE) == 0) {
				databaseSession.flush();
				databaseSession.clear();
			}
		}
		databaseSession.getTransaction().commit();
		databaseSession.flush();
		databaseSession.clear();
	}
}
