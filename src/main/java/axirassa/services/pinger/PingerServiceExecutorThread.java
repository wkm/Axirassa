
package axirassa.services.pinger;

import java.util.concurrent.Callable;import java.util.concurrent.ConcurrentLinkedQueue;import lombok.extern.slf4j.Slf4j;import org.hornetq.api.core.client.ClientMessage;import org.hornetq.api.core.client.ClientSession;import zanoccio.javakit.lambda.Function1;import axirassa.messaging.util.AbortSurvivorMessageException;import axirassa.messaging.util.CommonBackoffStrategies;import axirassa.messaging.util.ExponentialBackoffStrategy;import axirassa.messaging.util.InfiniteLoopExceptionSurvivor;import axirassa.model.PingerEntity;import axirassa.util.MessagingTools;
@Slf4j
public class PingerServiceExecutorThread implements Runnable {	private ClientSession messaging;
	private ConcurrentLinkedQueue<PingerServiceCoordinationMessage> pingRequestQueue;
	private ConcurrentLinkedQueue<PingerServiceCoordinationMessage> pingResponseQueue;


	public PingerServiceExecutorThread(ClientSession messaging,
	        ConcurrentLinkedQueue<PingerServiceCoordinationMessage> pingRequestQueue,
	        ConcurrentLinkedQueue<PingerServiceCoordinationMessage> pingResponseQueue) {
		this.messaging = messaging;
		this.pingRequestQueue = pingRequestQueue;
		this.pingResponseQueue = pingResponseQueue;
	}


	@Override
	public void run() {
		InfiniteLoopExceptionSurvivor executor = new InfiniteLoopExceptionSurvivor(
		        ExponentialBackoffStrategy.clone(CommonBackoffStrategies.EXPONENTIAL_BACKOFF_MESSAGING),
		        new Callable<Object>() {
			        @Override
			        public Object call() throws Exception {
				        pingRequestQueue.wait();
				        PingerServiceCoordinationMessage coordinationMessage = pingRequestQueue.poll();

				        if (coordinationMessage == null)
					        // keep waiting
					        return null;

				        // kill this survivor
				        if (coordinationMessage.isShutdownMessage()) {
					        log.info("Received shutdown message for thread");
					        throw new AbortSurvivorMessageException();
				        }

				        ClientMessage message = coordinationMessage.getClientMessage();				        PingerEntity entity = MessagingTools.fromMessageBytes(PingerEntity.class, message);				        
			        }
		        }, new Function1<Boolean, Throwable>() {
			        @Override
			        public Boolean call(Throwable e) throws Throwable {
				        log.error("Ignoring exception: ", e);
				        return null;
			        }
		        });

		try {
			executor.execute();
		} catch (Throwable t) {
			log.error("Exception", t);
		}
	}
}
