
package axirassa.webapp.ajax;

import java.io.IOException;

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

public class PingerStreamingService extends AbstractService {
	public PingerStreamingService (BayeuxServer server) {
		super(server, "pingerService", 5);
		spawnPingerService();
	}


	private void spawnPingerService () {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run () {
				try {
					pingerService();
				} catch (HornetQException e) {
					e.printStackTrace();
				}
			}
		});

		thread.start();
	}


	private void pingerService () throws HornetQException {
		ClientSession messagingSession = MessagingTools.getEmbeddedSession();
		ClientConsumer consumer = null;
		MessagingTopic topic = new MessagingTopic(messagingSession, "ax.account.#");
		consumer = topic.createConsumer();

		messagingSession.start();
		// System.out.println("Starting pinger service");

		while (true) {
			try {
				ClientMessage message = consumer.receive();
				HttpStatisticsEntity stat = InjectorService.rebuildMessage(message);
				if (stat == null) {
					System.err.println("received null message");
					continue;
				}

				System.out.println("RECEIVED MESSAGE: " + stat);

				PingerEntity pinger = stat.getPinger();

				ClientSessionChannel channel = getLocalSession().getChannel("/ax/pinger/" + pinger.getId());

				JSONObject jsonMessage = new JSONObject();

				jsonMessage.put("Date", stat.getTimestamp().toString());
				jsonMessage.put("StatusCode", stat.getStatusCode());
				jsonMessage.put("Latency", stat.getLatency());
				jsonMessage.put("TransferTime", stat.getResponseTime());
				jsonMessage.put("ResponseSize", stat.getResponseSize());

				channel.publish(jsonMessage.toCompactString());
				System.out.println("\t >>>> [ published ]");
			} catch (InvalidMessageClassException e) {
				System.err.println(e);
			} catch (IOException e) {
				System.err.println(e);
			} catch (ClassNotFoundException e) {
				System.err.println(e);
			} catch (IllegalStateException e) {
				System.err.println("IGNORING EXCEPTION FROM PINGER STREAMING SERVICE: ");
				e.printStackTrace(System.err);
			} catch (Exception e) {
				System.err.println(e);
			}
		}
	}
}
