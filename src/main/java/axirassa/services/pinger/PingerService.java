
package axirassa.services.pinger;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientProducer;
import org.hornetq.api.core.client.ClientSession;

import axirassa.config.Messaging;
import axirassa.services.Service;
import axirassa.services.exceptions.AxirassaServiceException;

public class PingerService implements Service {

	private final ConcurrentLinkedQueue<PingerServiceCoordinationMessage> pingRequestQueue = new ConcurrentLinkedQueue<PingerServiceCoordinationMessage>();
	private final ConcurrentLinkedQueue<PingerServiceCoordinationMessage> pingResponseQueue = new ConcurrentLinkedQueue<PingerServiceCoordinationMessage>();

	private final ClientSession session;


	public PingerService(ClientSession consumeSession) {
		this.session = consumeSession;
	}


	@Override
	public void execute() throws HornetQException, IOException, ClassNotFoundException, InterruptedException,
	        AxirassaServiceException {

		ClientConsumer requestQueueConsumer = session.createConsumer(Messaging.PINGER_REQUEST_QUEUE);
		ClientConsumer throttlingQueueConsumer = session.createConsumer(Messaging.PINGER_THROTTLING_QUEUE);
		ClientProducer responseQueueProducer = session.createProducer(Messaging.PINGER_RESPONSE_QUEUE);

		PingerServiceRequestThread requestThread = new PingerServiceRequestThread(requestQueueConsumer,
		        pingRequestQueue);

		PingerServiceResponseThread responseThread = new PingerServiceResponseThread(session, responseQueueProducer,
		        pingResponseQueue);

		PingerServiceRateMonitoringThread rateMonitoringThread = new PingerServiceRateMonitoringThread(
		        throttlingQueueConsumer, pingRequestQueue);

		new Thread(requestThread).start();
		new Thread(responseThread).start();
		new Thread(rateMonitoringThread).start();

	}
}
