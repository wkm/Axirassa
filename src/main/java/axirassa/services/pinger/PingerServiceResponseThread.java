
package axirassa.services.pinger;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;

import lombok.extern.slf4j.Slf4j;

import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientProducer;
import org.hornetq.api.core.client.ClientSession;

import zanoccio.javakit.lambda.Function1;
import axirassa.messaging.util.CommonBackoffStrategies;
import axirassa.messaging.util.ExponentialBackoffStrategy;
import axirassa.messaging.util.InfiniteLoopExceptionSurvivor;

@Slf4j
public class PingerServiceResponseThread implements Runnable {

	private final ClientSession messagingSession;
	private final ConcurrentLinkedQueue<PingerServiceCoordinationMessage> responseQueue;
	private final ClientProducer messageProducer;


	public PingerServiceResponseThread(ClientSession messagingSession, ClientProducer messageProducer,
	        ConcurrentLinkedQueue<PingerServiceCoordinationMessage> responseQueue) {
		this.messagingSession = messagingSession;
		this.responseQueue = responseQueue;
		this.messageProducer = messageProducer;
	}


	@Override
	public void run() {
		InfiniteLoopExceptionSurvivor executor = new InfiniteLoopExceptionSurvivor(
		        ExponentialBackoffStrategy.clone(CommonBackoffStrategies.EXPONENTIAL_BACKOFF_MESSAGING),
		        new Callable<Object>() {
			        @Override
			        public Object call() throws Exception {
				        responseQueue.wait();

				        PingerServiceCoordinationMessage message = responseQueue.poll();

				        if (message == null)
					        return null;

				        if (message.getStatistic() == null) {
					        log.error("Pinger response thread received coordination message without statistic");
					        return null;
				        }

				        sendResponseMessage(message);
				        return null;
			        }
		        }, new Function1<Object, Throwable>() {
			        public Object call(Throwable e) {
				        if (e instanceof InterruptedException)
					        return null;

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


	private void sendResponseMessage(PingerServiceCoordinationMessage message) throws HornetQException, IOException {
		ClientMessage responseMessage = messagingSession.createMessage(true);
		responseMessage.getBodyBuffer().writeBytes(message.getStatistic().toBytes());

		log.info("Sending response message");
		messageProducer.send(responseMessage);
		messageProducer.send(message.getStatistic().getBroadcastAddress(), responseMessage);

		message.getClientMessage().acknowledge();
		messagingSession.commit();
	}
}
