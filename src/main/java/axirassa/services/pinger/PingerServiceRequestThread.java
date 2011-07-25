
package axirassa.services.pinger;

import java.util.concurrent.Callable;

import lombok.extern.slf4j.Slf4j;

import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientMessage;

import zanoccio.javakit.lambda.Function1;
import axirassa.messaging.util.CommonBackoffStrategies;
import axirassa.messaging.util.InfiniteLoopExceptionSurvivor;

@Slf4j
public class PingerServiceRequestThread implements Runnable {

	private ClientConsumer requestQueueConsumer;
	private PingerServiceCoordinator pingRequestQueue;


	public PingerServiceRequestThread(ClientConsumer requestQueueConsumer,
			PingerServiceCoordinator pingRequestQueue) {
		this.requestQueueConsumer = requestQueueConsumer;
		this.pingRequestQueue = pingRequestQueue;

		log.info("Starting");
	}


	@Override
	public void run() {
		InfiniteLoopExceptionSurvivor executor = new InfiniteLoopExceptionSurvivor(
		        CommonBackoffStrategies.EXPONENTIAL_BACKOFF_MESSAGING(), new Callable<Object>() {
			        @Override
			        public Object call() throws Exception {
				        ClientMessage message = requestQueueConsumer.receive();
				        PingerServiceCoordinationMessage coordinationMessage = PingerServiceCoordinationMessage
				                .pingMessage(message);
				        
				        synchronized (pingRequestQueue) {
					        pingRequestQueue.append(coordinationMessage);
					        pingRequestQueue.notify();

					        log.info("PINGER SERVICE REQUEST QUEUE: {}", pingRequestQueue.size());
				        }

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
			log.error("Exception ", t);
		}
	}
}
