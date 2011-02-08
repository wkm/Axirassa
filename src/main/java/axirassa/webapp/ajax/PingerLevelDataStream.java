
package axirassa.webapp.ajax;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.directwebremoting.Browser;
import org.directwebremoting.ScriptSessions;
import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientSession;

import axirassa.model.HttpStatisticsEntity;
import axirassa.model.PingerEntity;
import axirassa.services.InjectorService;
import axirassa.services.exceptions.InvalidMessageClassException;
import axirassa.util.MessagingTools;
import axirassa.util.MessagingTopic;

public class PingerLevelDataStream {
	protected static final Logger log = Logger.getRootLogger();
	private long pingerId;
	private long userId;

	private MessagingTopic topic;


	public void subscribe(long userId, long pingerId) throws InterruptedException, HornetQException,
	        InvalidMessageClassException, IOException, ClassNotFoundException {
		this.pingerId = pingerId;
		this.userId = userId;

		ClientSession session = MessagingTools.getEmbeddedSession();
		String address = PingerEntity.createBroadcastQueueName(userId, pingerId);
		System.err.println("#### subscribing to " + address);

		topic = new MessagingTopic(session, address);
		session.start();
		ClientConsumer consumer = topic.createConsumer();

		while (true) {
			System.err.println("#### awaiting message");
			ClientMessage message = consumer.receive();
			System.err.println("#### message received: " + message);

			HttpStatisticsEntity entity = InjectorService.rebuildMessage(message);
			streamData(entity.getResponseTime());
		}
	}


	public void streamData(final int responseTime) {
		Browser.withCurrentPage(new Runnable() {
			@Override
			public void run() {
				ScriptSessions.addFunctionCall("addDataPoint", responseTime);
			}
		});
	}
}
