
package axirassa.services.pinger;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientProducer;
import org.hornetq.api.core.client.ClientSession;

import zanoccio.javakit.lambda.Function1;
import axirassa.config.Messaging;
import axirassa.messaging.util.CommonBackoffStrategies;
import axirassa.messaging.util.ExponentialBackoffStrategy;
import axirassa.messaging.util.InfiniteLoopExceptionSurvivor;
import axirassa.model.HttpStatisticsEntity;
import axirassa.services.Service;
import axirassa.util.MessagingTools;
import axirassa.util.MessagingTopic;

public class PingerThrottlingService implements Service {
	private final BandwidthMeasurer measurer = new BandwidthMeasurer(60000);
	private final ClientSession messaging;


	public PingerThrottlingService(ClientSession session) {
		this.messaging = session;
	}


	@Override
	public void execute() throws Throwable {
		ClientProducer throttlingProducer = messaging.createProducer(Messaging.PINGER_THROTTLING_QUEUE);

		MessagingTopic pingerResponseTopic = new MessagingTopic(messaging, "ax.account.#");
		ClientConsumer pingerResponseConsumer = pingerResponseTopic.createConsumer();

		new Thread(new PingerBandwidthMeasurementPopulatorThread(pingerResponseConsumer, measurer)).run();

		ScheduledThreadPoolExecutor scheduledExecutor = new ScheduledThreadPoolExecutor(1);
		scheduledExecutor.scheduleAtFixedRate(new PingerThreadCountAdjusterThread(throttlingProducer, measurer), 0,
		                                      100, TimeUnit.MILLISECONDS);
	}
}

@Slf4j
class PingerThreadCountAdjusterThread implements Runnable {
	private final int currentThreadCount = 0;
	private final ClientProducer throttlingProducer;
	private final BandwidthMeasurer measurer;


	PingerThreadCountAdjusterThread(ClientProducer throttlingProducer, BandwidthMeasurer measurer) {
		this.measurer = measurer;
		this.throttlingProducer = throttlingProducer;
	}


	@Override
	public void run() {
		printCurrentStatus();
	}


	private void printCurrentStatus() {
		// TODO Auto-generated method stub

	}
}

/**
 * Watches for pinger responses and feeds the data into a
 * {@link BandwidthMeasurer} object
 * 
 * @author wiktor
 */
@Slf4j
class PingerBandwidthMeasurementPopulatorThread implements Runnable {

	private final ClientConsumer consumer;
	private final BandwidthMeasurer measurer;


	PingerBandwidthMeasurementPopulatorThread(ClientConsumer consumer, BandwidthMeasurer measurer) {
		this.consumer = consumer;
		this.measurer = measurer;
	}


	@Override
	public void run() {
		InfiniteLoopExceptionSurvivor executor = new InfiniteLoopExceptionSurvivor(
		        ExponentialBackoffStrategy.clone(CommonBackoffStrategies.EXPONENTIAL_BACKOFF_MESSAGING),
		        new Callable<Object>() {
			        @Override
			        public Object call() throws Exception {
				        ClientMessage message = consumer.receive();
				        HttpStatisticsEntity statistic = MessagingTools.fromMessageBytes(HttpStatisticsEntity.class,
				                                                                         message);

				        measurer.addDownload(statistic.getResponseSize(), System.currentTimeMillis(),
				                             statistic.getResponseTime());

				        return null;
			        }
		        }, new Function1<Boolean, Throwable>() {
			        public Boolean call(Throwable e) {
				        log.error("Exception: ", e);
				        return null;
			        }
		        });

		try {
			executor.execute();
		} catch (Throwable t) {
			log.error("Exception: ", t);
		}
	}
}
