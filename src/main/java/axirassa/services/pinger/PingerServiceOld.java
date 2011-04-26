
package axirassa.services.pinger;

import java.io.IOException;

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

public class PingerServiceOld implements Service {

	private final ClientSession session;
	private final HttpPinger pinger;


	public PingerServiceOld (ClientSession consumeSession) {
		this.session = consumeSession;

		pinger = new HttpPinger();
	}


	@Override
	public void execute () throws HornetQException, IOException, ClassNotFoundException, InterruptedException,
	        AxirassaServiceException {
		// we have to start before reading messages
		session.start();

		ClientConsumer consumer = session.createConsumer(Messaging.PINGER_REQUEST_QUEUE);
		ClientProducer producer = session.createProducer(Messaging.PINGER_RESPONSE_QUEUE);

		try {
			while (true) {
				ClientMessage message = consumer.receive();

				byte[] buffer = new byte[message.getBodyBuffer().readableBytes()];
				message.getBodyBuffer().readBytes(buffer);

				Object rawobject = AutoSerializingObject.fromBytes(buffer);
				if (rawobject instanceof PingerEntity) {
					PingerEntity request = (PingerEntity) rawobject;
					HttpStatisticsEntity statistic = pinger.ping(request);

					if (statistic != null)
						sendResponseMessages(producer, statistic);

					System.out.println(request.getUrl() + " TRIGGERS: " + pinger.getTriggers());
				} else
					throw new InvalidMessageClassException(PingerEntity.class, rawobject);

				message.acknowledge();
				session.commit();
			}
		} finally {
			if (consumer != null)
				consumer.close();

			if (session != null)
				session.stop();
		}
	}


	private void sendResponseMessages (ClientProducer producer, HttpStatisticsEntity statistic) throws IOException,
	        HornetQException {
		// send a message to the primary pinger response queue for injection
		ClientMessage message = session.createMessage(true);
		message.getBodyBuffer().writeBytes(statistic.toBytes());
		producer.send(message);

		// send a message to the broadcast address for this pinger
		producer.send(getBroadcastAddress(statistic), message);
	}


	private String getBroadcastAddress (HttpStatisticsEntity statistic) {
		String address = PingerEntity.createBroadcastQueueName(statistic.getPinger().getUser().getId(), statistic
		        .getPinger().getId());
		return address;
	}
}
