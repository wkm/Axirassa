
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

public class PingerService implements Service {

	private final ClientSession session;
	private final HTTPPinger pinger;


	public PingerService(ClientSession consumeSession) {
		this.session = consumeSession;

		pinger = new HTTPPinger();
	}


	@Override
	public void execute() throws HornetQException, IOException, ClassNotFoundException, InterruptedException,
	        AxirassaServiceException {
		// we have to start before reading messages
		session.start();

		ClientConsumer consumer = session.createConsumer(Messaging.PINGER_REQUEST_QUEUE);
		ClientProducer producer = session.createProducer(Messaging.PINGER_RESPONSE_QUEUE);

		try {
			while (true) {
				ClientMessage message = consumer.receive();
				message.acknowledge();

				byte[] buffer = new byte[message.getBodyBuffer().readableBytes()];
				message.getBodyBuffer().readBytes(buffer);

				Object rawobject = AutoSerializingObject.fromBytes(buffer);
				if (rawobject instanceof PingerEntity) {
					PingerEntity request = (PingerEntity) rawobject;
					HttpStatisticsEntity statistic = pinger.ping(request);

					sendResponseMessage(producer, statistic);
				} else
					throw new InvalidMessageClassException(PingerEntity.class, rawobject);
			}
		} finally {
			consumer.close();
			session.stop();
		}
	}


	private void sendResponseMessage(ClientProducer producer, HttpStatisticsEntity statistic) throws IOException,
	        HornetQException {
		ClientMessage message = session.createMessage(false);
		message.getBodyBuffer().writeBytes(statistic.toBytes());
		producer.send(message);
	}
}
