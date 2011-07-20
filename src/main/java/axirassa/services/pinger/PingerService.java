
package axirassa.services.pinger;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientProducer;
import org.hornetq.api.core.client.ClientSession;

import axirassa.config.Messaging;
import axirassa.model.HttpStatisticsEntity;
import axirassa.model.PingerEntity;
import axirassa.services.Service;
import axirassa.services.exceptions.AxirassaServiceException;
import axirassa.services.exceptions.InvalidMessageClassException;
import axirassa.util.AutoSerializingObject;

public class PingerService implements Service {

	private final ConcurrentLinkedQueue<PingerServiceCoordinationMessage> pingRequestQueue = new ConcurrentLinkedQueue<PingerServiceCoordinationMessage>();
	private final ConcurrentLinkedQueue<PingerServiceCoordinationMessage> pingResponseQueue = new ConcurrentLinkedQueue<PingerServiceCoordinationMessage>();

	private final ClientSession session;
	private final HttpPinger pinger;

	private int pingCount = 0;


	public PingerService (ClientSession consumeSession) {
		this.session = consumeSession;

		pinger = new HttpPinger();
	}


	@Override
	public void execute () throws HornetQException, IOException, ClassNotFoundException, InterruptedException,
	        AxirassaServiceException {

		ClientProducer responseQueueProducer = session.createProducer(Messaging.PINGER_RESPONSE_QUEUE);

		PingerServiceResponseThread responseThread = new PingerServiceResponseThread(session, responseQueueProducer,
		        pingResponseQueue);
		PingerServiceRateMonitoringThread rateMonitoringThread = new PingerServiceRateMonitoringThread();

		new Thread(responseThread).start();
		new Thread(rateMonitoringThread).start();

		// we have to start before reading messages
		session.start();

		ClientConsumer consumer = session.createConsumer(Messaging.PINGER_REQUEST_QUEUE);
		ClientProducer producer = session.createProducer(Messaging.PINGER_RESPONSE_QUEUE);

		try {
			while (true) {
				ClientMessage message = consumer.receive();

				byte[] buffer = new byte[message.getBodyBuffer().readableBytes()];
				message.getBodyBuffer().readBytes(buffer);

				message.acknowledge();
				session.commit();

				Object rawobject = AutoSerializingObject.fromBytes(buffer);
				if (rawobject instanceof PingerEntity) {
					pingCount++;
					PingerEntity request = (PingerEntity) rawobject;

					System.out.println("PING " + request.getUrl());

					HttpStatisticsEntity statistic = pinger.ping(request);

					if (statistic != null)
						sendResponseMessages(producer, statistic);

					System.out.printf("#%07d %50s  TRIGGERS: %s\n", pingCount, request.getUrl(), pinger.getTriggers());
				} else
					throw new InvalidMessageClassException(PingerEntity.class, rawobject);
			}
		} finally {
			if (consumer != null)
				consumer.close();

			if (session != null)
				session.stop();
		}
	}
}
