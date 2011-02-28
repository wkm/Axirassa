
package axirassa.webapp.services.internal;

import java.io.IOException;
import java.util.HashMap;

import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientProducer;

import axirassa.config.Messaging;
import axirassa.messaging.SmsRequestMessage;
import axirassa.services.phone.PhoneTemplate;
import axirassa.webapp.services.MessagingSession;
import axirassa.webapp.services.SmsNotifyService;

public class SmsNotifyServiceImpl implements SmsNotifyService {
	private final MessagingSession messagingSession;
	private final ClientProducer producer;

	private PhoneTemplate template;
	private final HashMap<String, Object> attributes = new HashMap<String, Object>();
	private String phoneNumber;


	public SmsNotifyServiceImpl(MessagingSession messagingSession) throws HornetQException {
		this.messagingSession = messagingSession;
		producer = messagingSession.createProducer(Messaging.NOTIFY_SMS_REQUEST);
	}


	@Override
	public void startMessage(PhoneTemplate template) {
		reset();
		this.template = template;
	}


	private void reset() {
		attributes.clear();
	}


	@Override
	public void addAttribute(String key, String value) {
		attributes.put(key, value);
	}


	@Override
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	@Override
	public void send() throws HornetQException, IOException {
		ClientMessage message = messagingSession.createMessage(true);

		SmsRequestMessage request = new SmsRequestMessage(phoneNumber, template);
		request.addAttributes(attributes);

		message.getBodyBuffer().writeBytes(request.toBytes());
		producer.send(message);

		reset();
	}
}
