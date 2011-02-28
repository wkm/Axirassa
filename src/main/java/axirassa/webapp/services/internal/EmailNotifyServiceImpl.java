
package axirassa.webapp.services.internal;

import java.io.IOException;

import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientProducer;

import axirassa.config.Messaging;
import axirassa.messaging.EmailRequestMessage;
import axirassa.services.email.EmailTemplate;
import axirassa.webapp.services.EmailNotifyService;
import axirassa.webapp.services.MessagingSession;

public class EmailNotifyServiceImpl implements EmailNotifyService {
	private final MessagingSession messagingSession;
	private final ClientProducer producer;

	private EmailRequestMessage request;


	public EmailNotifyServiceImpl(MessagingSession messagingSession) throws HornetQException {
		this.messagingSession = messagingSession;
		producer = messagingSession.createProducer(Messaging.NOTIFY_EMAIL_REQUEST);
	}


	@Override
	public void startMessage(EmailTemplate template) {
		request = new EmailRequestMessage(template);
	}


	@Override
	public void addAttribute(String key, String value) {
		request.addAttribute(key, value);
	}


	@Override
	public void send() throws HornetQException, IOException {
		ClientMessage message = messagingSession.createMessage(true);
		message.getBodyBuffer().writeBytes(request.toBytes());
		producer.send(message);

		request = null;
	}


	@Override
	public void setToAddress(String email) {
		request.setToAddress(email);
	}
}
