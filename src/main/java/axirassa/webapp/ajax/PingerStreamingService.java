
package axirassa.webapp.ajax;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

import org.apache.tapestry5.json.JSONObject;
import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.server.AbstractService;
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

@Slf4j
public class PingerStreamingService extends AbstractService {

	public PingerStreamingService(BayeuxServer server) {
		super(server, "pingerService", 5);
		spawnPingerService();
	}


	private void spawnPingerService() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					pingerService();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		thread.start();
	}


	private void pingerService() throws Exception {
		ClientSession messagingSession = MessagingTools.getEmbeddedSession();
		ClientConsumer consumer = null;
		MessagingTopic topic = new MessagingTopic(messagingSession, "ax.account.#");
		consumer = topic.createConsumer();

		messagingSession.start();
		// System.out.println("Starting pinger service");

		while (true) {
			try {
				consumer = reopenConsumerIfClosed(topic, consumer);

				ClientMessage message = consumer.receive();
				HttpStatisticsEntity stat = InjectorService.rebuildMessage(message);
				if (stat == null) {
					log.warn("received null message");
					continue;
				}

				log.trace("received message: {}", stat);

				PingerEntity pinger = stat.getPinger();

				ClientSessionChannel channel = getLocalSession().getChannel("/ax/pinger/" + pinger.getId());

				JSONObject jsonMessage = new JSONObject();

				jsonMessage.put("Date", stat.getTimestamp().toString());
				jsonMessage.put("StatusCode", stat.getStatusCode());
				jsonMessage.put("Latency", stat.getLatency());
				jsonMessage.put("TransferTime", stat.getResponseTime());
				jsonMessage.put("ResponseSize", stat.getResponseSize());

				channel.publish(jsonMessage.toCompactString());
			} catch (InvalidMessageClassException e) {
				log.error("Exception", e);
			} catch (IOException e) {
				log.error("Exception", e);
			} catch (ClassNotFoundException e) {
				log.error("Exception", e);
			} catch (IllegalStateException e) {
				log.error("IGNORING EXCEPTION FROM PINGER STREAMING SERVICE: ", e);
			} catch (Exception e) {
				log.error("Exception", e);
			}
		}
	}


	private ClientConsumer reopenConsumerIfClosed(MessagingTopic topic, ClientConsumer consumer)
	        throws HornetQException {
		if (consumer.isClosed()) {
			log.warn("Consumer is closed, re-opening");
			return topic.createConsumer();
		} else
			return consumer;
	}
}
