
package axirassa.services.pinger;

import java.util.concurrent.Callable;

import lombok.extern.slf4j.Slf4j;

import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientMessage;

import zanoccio.javakit.lambda.Function1;
import axirassa.messaging.AbstractPingerThrottlingMessage;
import axirassa.messaging.PingerDownThrottleMessage;
import axirassa.messaging.PingerUpThrottlingMessage;
import axirassa.messaging.util.CommonBackoffStrategies;
import axirassa.messaging.util.InfiniteLoopExceptionSurvivor;
import axirassa.util.MessagingTools;

@Slf4j
public class PingerServiceRateMonitoringThread implements Runnable {
	private PingerServiceCoordinator pingRequestQueue;
	private ClientConsumer throttlingQueueConsumer;

	private int threadsStarted = 0;
	private int threadsStopped = 0;


	public PingerServiceRateMonitoringThread(ClientConsumer throttlingQueueConsumer,
	        PingerServiceCoordinator pingRequestQueue, PingerServiceCoordinator pingRequestQueue) {
		this.throttlingQueueConsumer = throttlingQueueConsumer;
		this.pingRequestQueue = pingRequestQueue;
	}


	@Override
	public void run() {
		InfiniteLoopExceptionSurvivor executor = new InfiniteLoopExceptionSurvivor(
		        CommonBackoffStrategies.EXPONENTIAL_BACKOFF_MESSAGING(), new Callable<Object>() {
			        @Override
			        public Object call() throws Exception {
				        ClientMessage clientMessage = throttlingQueueConsumer.receive();
				        AbstractPingerThrottlingMessage message = MessagingTools
				                .fromMessageBytes(AbstractPingerThrottlingMessage.class, message);

				        if (message instanceof PingerDownThrottleMessage)
					        pingRequestQueue.add(PingerServiceCoordinationMessage.shutdownMessage());

				        if (message instanceof PingerUpThrottlingMessage) {
					        PingerServiceExecutorThread executor = new PingerServiceExecutorThread(null,
					                pingRequestQueue, pingResponseQueue);
				        }
				        // spawn a new thread

				        clientMessage.acknowledge();

				        return null;
			        }
		        }, new Function1<Boolean, Throwable>() {
			        @Override
			        public Boolean call(Throwable t) throws Throwable {
				        t.printStackTrace(System.err);
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
