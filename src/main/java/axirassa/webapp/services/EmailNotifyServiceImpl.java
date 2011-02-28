
package axirassa.webapp.services;

import java.io.IOException;

import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientProducer;

import axirassa.config.Messaging;
import axirassa.messaging.EmailRequestMessage;
import axirassa.services.email.EmailTemplate;

public class EmailNotifyServiceImpl implements EmailNotifyService {
	private final MessagingSession messagingSession;
	private final ClientProducer producer;

	private EmailRequestMessage request;


	public EmailNotifyServiceImpl(MessagingSession messagingSession) throws HornetQException {
		this.messagingSession = messagingSession;
		producer = messagingSession.createProducer(Messaging.NOTIFY_EMAIL_REQUEST);
	}


	/* (non-Javadoc)
     * @see axirassa.webapp.services.EmailNotifyService#startMessage(axirassa.services.email.EmailTemplate)
     */
	@Override
    public void startMessage(EmailTemplate template) {
		request = new EmailRequestMessage(template);
	}


	/* (non-Javadoc)
     * @see axirassa.webapp.services.EmailNotifyService#addAttribute(java.lang.String, java.lang.String)
     */
	@Override
    public void addAttribute(String key, String value) {
		request.addAttribute(key, value);
	}


	/* (non-Javadoc)
     * @see axirassa.webapp.services.EmailNotifyService#send()
     */
	@Override
    public void send() throws HornetQException, IOException {
		ClientMessage message = messagingSession.createMessage(true);
		message.getBodyBuffer().writeBytes(request.toBytes());
		producer.send(message);

		request = null;
	}


	/* (non-Javadoc)
     * @see axirassa.webapp.services.EmailNotifyService#setToAddress(java.lang.String)
     */
	@Override
    public void setToAddress(String email) {
		request.setToAddress(email);
	}
}
