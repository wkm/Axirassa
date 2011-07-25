
package axirassa.services.pinger;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;

import lombok.extern.slf4j.Slf4j;

import org.hornetq.api.core.client.ClientConsumer;

import zanoccio.javakit.lambda.Function1;
import axirassa.messaging.util.CommonBackoffStrategies;
import axirassa.messaging.util.InfiniteLoopExceptionSurvivor;

@Slf4j
public class PingerServiceRateMonitoringThread implements Runnable {
	private ConcurrentLinkedQueue<PingerServiceCoordinationMessage> pingRequestQueue;
	private ClientConsumer throttlingQueueConsumer;

	private int threadsStarted = 0;
	private int threadsStopped = 0;


	public PingerServiceRateMonitoringThread(ClientConsumer throttlingQueueConsumer,
	        ConcurrentLinkedQueue<PingerServiceCoordinationMessage> pingRequestQueue) {
		this.throttlingQueueConsumer = throttlingQueueConsumer;
		this.pingRequestQueue = pingRequestQueue;
	}


	@Override
	public void run() {
		InfiniteLoopExceptionSurvivor executor = new InfiniteLoopExceptionSurvivor(
		        CommonBackoffStrategies.EXPONENTIAL_BACKOFF_MESSAGING(), 
		        new Callable<Object>() {
					@Override
                    public Object call() throws Exception {
	                    // TODO Auto-generated method stub
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
