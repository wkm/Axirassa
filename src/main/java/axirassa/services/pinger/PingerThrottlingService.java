
package axirassa.services.pinger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientProducer;
import org.hornetq.api.core.client.ClientSession;

import zanoccio.javakit.ListUtilities;
import zanoccio.javakit.lambda.Function1;
import axirassa.config.Messaging;
import axirassa.messaging.AbstractPingerThrottlingMessage;
import axirassa.messaging.PingerDownThrottleMessage;
import axirassa.messaging.PingerUpThrottlingMessage;
import axirassa.messaging.util.CommonBackoffStrategies;
import axirassa.messaging.util.InfiniteLoopExceptionSurvivor;
import axirassa.model.HttpStatisticsEntity;
import axirassa.services.Service;
import axirassa.services.pinger.BandwidthThreadAllocator.BandwidthThreadAllocatorException;
import axirassa.util.MessagingTools;
import axirassa.util.MessagingTopic;

public class PingerThrottlingService implements Service {
	private final BandwidthMeasurer measurer;
	private final BandwidthThreadAllocator threadAllocator;
	private final ClientSession messaging;


	public PingerThrottlingService(ClientSession session, BandwidthMeasurer bandwidthMeasurer,
	        BandwidthThreadAllocator threadAllocator) {
		this.messaging = session;
		this.measurer = bandwidthMeasurer;
		this.threadAllocator = threadAllocator;
	}


	@Override
	public void execute() throws Throwable {
		ClientProducer throttlingProducer = messaging.createProducer(Messaging.PINGER_THROTTLING_QUEUE);

		MessagingTopic pingerResponseTopic = new MessagingTopic(messaging, "ax.account.#");
		ClientConsumer pingerResponseConsumer = pingerResponseTopic.createConsumer();

		Thread measurementThread = new Thread(new PingerBandwidthMeasurementPopulatorThread(pingerResponseConsumer,
		        measurer));
		measurementThread.start();

		ScheduledThreadPoolExecutor scheduledExecutor = new ScheduledThreadPoolExecutor(1);
		Runnable adjusterThread = new PingerThreadCountAdjusterThread(messaging, throttlingProducer, measurer,
		        threadAllocator);
		scheduledExecutor.scheduleAtFixedRate(adjusterThread, 100, 100, TimeUnit.MILLISECONDS);

		measurementThread.join();
	}
}

@Slf4j
class PingerThreadCountAdjusterThread implements Runnable {
	private final ClientSession messaging;
	private final ClientProducer throttlingProducer;
	private final BandwidthMeasurer measurer;
	private final BandwidthThreadAllocator bandwidthAllocator;


	PingerThreadCountAdjusterThread(ClientSession messaging, ClientProducer throttlingProducer,
	        BandwidthMeasurer measurer, BandwidthThreadAllocator bandwidthAllocator) {
		log.info("Created");

		this.messaging = messaging;
		this.measurer = measurer;
		this.throttlingProducer = throttlingProducer;
		this.bandwidthAllocator = bandwidthAllocator;
	}


	@Override
	public void run() {
		try {
			long currentRate = measurer.currentRate(1000, System.currentTimeMillis());
			bandwidthAllocator.addBandwidthMeasurement(currentRate);
			int threadDelta = bandwidthAllocator.getTargetThreadCountDelta();

			printCurrentStatus(threadDelta);

			for (AbstractPingerThrottlingMessage messageBody : createMessages(threadDelta)) {
				System.out.println("\t"+messageBody);
				ClientMessage message = messaging.createMessage(true);
				message.getBodyBuffer().writeBytes(messageBody.toBytes());
				throttlingProducer.send(message);
			}
			
			bandwidthAllocator.applyThreadCountDelta(threadDelta);
		} catch (HornetQException e) {
			e.printStackTrace(System.err);
		} catch (IOException e) {
			e.printStackTrace(System.err);
		} catch (BandwidthThreadAllocatorException e) {
	        e.printStackTrace();
        }
	}


	private PingerDownThrottleMessage downThrottleMessage = new PingerDownThrottleMessage();
	private PingerUpThrottlingMessage upThrottlingMessage = new PingerUpThrottlingMessage();


	private List<AbstractPingerThrottlingMessage> createMessages(int threadDelta) {
		ArrayList<AbstractPingerThrottlingMessage> list = new ArrayList<AbstractPingerThrottlingMessage>(threadDelta);

		if (threadDelta == 0)
			return Collections.EMPTY_LIST;

		if (threadDelta < 0)
			return ListUtilities.padRight(list, -threadDelta, downThrottleMessage);
		else
			return ListUtilities.padRight(list, threadDelta, upThrottlingMessage);
	}


	private void printCurrentStatus(int threadDelta) {
		String msg = String.format("#### THREADS: %4d DELTA: %5d BANDWIDTH: %8d",
		                           bandwidthAllocator.getCurrentThreads(), threadDelta,
		                           measurer.currentRate(1000, System.currentTimeMillis()));
		log.info(msg);
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

		log.info("Created");
	}


	@Override
	public void run() {
		InfiniteLoopExceptionSurvivor executor = new InfiniteLoopExceptionSurvivor(
		        CommonBackoffStrategies.EXPONENTIAL_BACKOFF_MESSAGING(), new Callable<Object>() {
			        @Override
			        public Object call() throws Exception {
				        ClientMessage message = consumer.receive();
				        HttpStatisticsEntity statistic = MessagingTools.fromMessageBytes(HttpStatisticsEntity.class,
				                                                                         message);

				        if (statistic == null)
					        return null;

				        log.info("Adjusting bandwidth measurements with: {} over {} ", statistic.getResponseSize(),
				                 statistic.getResponseTime());

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
