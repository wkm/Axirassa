
package axirassa.services.pinger;

import java.io.IOException;

import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientProducer;
import org.hornetq.api.core.client.ClientSession;

import axirassa.config.Messaging;
import axirassa.messaging.PingerRequestMessage;
import axirassa.messaging.PingerResponseMessage;
import axirassa.services.Service;
import axirassa.services.exceptions.PingerServiceException;
import axirassa.util.AutoSerializingObject;

public class PingerService implements Service {

	private final ClientSession messagingSession;
	private final HTTPPinger pinger;


	public PingerService(ClientSession messagingSession) {
		this.messagingSession = messagingSession;

		pinger = new HTTPPinger();
	}


	@Override
	public void execute() throws HornetQException, IOException, ClassNotFoundException, InterruptedException,
	        PingerServiceException {
		// we have to start before reading messages
		messagingSession.start();

		ClientProducer responseProducer = messagingSession.createProducer(Messaging.PINGER_RESPONSE_QUEUE);
		ClientConsumer requestConsumer = messagingSession.createConsumer(Messaging.PINGER_REQUEST_QUEUE);

		while (true) {
			ClientMessage message = requestConsumer.receiveImmediate();
			if (message == null) {
				System.out.println("No more messages.");
				break;
			}

			byte[] buffer = new byte[message.getBodyBuffer().readableBytes()];
			message.getBodyBuffer().readBytes(buffer);

			Object rawobject = AutoSerializingObject.fromBytes(buffer);
			if (rawobject instanceof PingerRequestMessage) {
				PingerRequestMessage request = (PingerRequestMessage) rawobject;
				PingerResponseMessage response = pinger.ping(request.getUrl());

				sendResponseMessage(responseProducer, response);
			}

			Thread.sleep(2000);
		}

		responseProducer.close();
		requestConsumer.close();

		messagingSession.stop();
	}


	private void sendResponseMessage(ClientProducer responseProducer, PingerResponseMessage response)
	        throws IOException, HornetQException {
		ClientMessage message = messagingSession.createMessage(false);
		message.getBodyBuffer().writeBytes(response.toBytes());
		responseProducer.send(message);
	}
}
