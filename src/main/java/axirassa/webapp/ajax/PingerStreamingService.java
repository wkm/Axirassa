
package axirassa.webapp.ajax;

import java.util.concurrent.Callable;

import lombok.extern.slf4j.Slf4j;

import org.apache.tapestry5.json.JSONObject;
import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.server.AbstractService;
import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientSession;

import zanoccio.javakit.lambda.Function1;
import axirassa.messaging.util.CommonBackoffStrategies;
import axirassa.messaging.util.ExponentialBackoffStrategy;
import axirassa.messaging.util.InfiniteLoopExceptionSurvivor;
import axirassa.model.HttpStatisticsEntity;
import axirassa.model.PingerEntity;
import axirassa.services.InjectorService;
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
				} catch (Throwable e) {
					log.error("Exception: ", e);
				}
			}
		});

		thread.start();
	}


	private void pingerService() throws Throwable {
		ClientSession messagingSession = MessagingTools.getEmbeddedSession();
		
		final MessagingTopic topic = new MessagingTopic(messagingSession, "ax.account.#");

		messagingSession.start();

		InfiniteLoopExceptionSurvivor executor = new InfiniteLoopExceptionSurvivor(ExponentialBackoffStrategy.clone(CommonBackoffStrategies.EXPONENTIAL_BACKOFF_MESSAGING), 
            new Callable<Object>() {
				ClientConsumer consumer = null; 
				
    			@Override
                public Object call() throws Exception {
    				System.out.println("STARTING STREAMING SERVICE");
    				consumer = reopenConsumerIfClosed(topic, consumer);

    				ClientMessage message = consumer.receive();
    				System.out.println("RECEIVED MESSAGE");
    				HttpStatisticsEntity stat = InjectorService.rebuildMessage(message);
    				if (stat == null) {
    					log.warn("received null message");
    					return null;
    				}

    				log.warn("received message: {}", stat);

    				PingerEntity pinger = stat.getPinger();

    				ClientSessionChannel channel = getLocalSession().getChannel("/ax/pinger/" + pinger.getId());

    				JSONObject jsonMessage = new JSONObject();

    				jsonMessage.put("Date", stat.getTimestamp().toString());
    				jsonMessage.put("StatusCode", stat.getStatusCode());
    				jsonMessage.put("Latency", stat.getLatency());
    				jsonMessage.put("TransferTime", stat.getResponseTime());
    				jsonMessage.put("ResponseSize", stat.getResponseSize());

    				channel.publish(jsonMessage.toCompactString());
    				
    				return null;
                }
			}, 
			new Function1<Boolean, Throwable>() {
    			public Boolean call(Throwable e) {
    				if(e instanceof IllegalStateException)
    					log.error("IGNORING EXCEPTION FROM PINGER STREAMING SERVICE: ", e);
    				if(e instanceof Exception)
    					log.error("Exception", e);
    				
    				return null;
    			}
    		}
		);
		
		executor.execute();
	}


	private ClientConsumer reopenConsumerIfClosed(MessagingTopic topic, ClientConsumer consumer)
	        throws HornetQException {
		if (consumer == null)
			return topic.createConsumer();
			
		if(consumer.isClosed()) {
			log.warn("Consumer is closed, re-opening");
			return topic.createConsumer();
		} else
			return consumer;
	}
}
