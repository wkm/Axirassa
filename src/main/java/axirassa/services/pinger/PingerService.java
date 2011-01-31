
package axirassa.services.pinger;

import java.io.IOException;

import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientSession;

import axirassa.config.Messaging;
import axirassa.services.Service;
import axirassa.util.AutoSerializingObject;
import axirassa.util.Meta;

public class PingerService implements Service {

	private final ClientSession messagingSession;


	public PingerService(ClientSession messagingSession) {
		this.messagingSession = messagingSession;
	}


	@Override
	public void execute() throws HornetQException, IOException, ClassNotFoundException, InterruptedException {
		// we have to start before reading messages
		messagingSession.start();

		ClientConsumer consumer = messagingSession.createConsumer(Messaging.PINGER_REQUEST_QUEUE);
		while (true) {
			ClientMessage message = consumer.receiveImmediate();
			if (message == null) {
				System.out.println("No more messages.");
				break;
			}

			byte[] buffer = new byte[message.getBodyBuffer().readableBytes()];
			message.getBodyBuffer().readBytes(buffer);

			Object rawobject = AutoSerializingObject.fromBytes(buffer);
			Meta.inspect(rawobject);

			Thread.sleep(2000);
		}

		messagingSession.stop();
	}
}
