
package axirassa.webapp.services.internal;

import java.io.IOException;
import java.util.HashMap;

import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientProducer;

import axirassa.config.Messaging;
import axirassa.messaging.VoiceRequestMessage;
import axirassa.services.phone.PhoneTemplate;
import axirassa.webapp.services.MessagingSession;
import axirassa.webapp.services.VoiceNotifyService;

public class VoiceNotifyServiceImpl implements VoiceNotifyService {
	private final MessagingSession messagingSession;
	private final ClientProducer producer;

	private PhoneTemplate template;
	private final HashMap<String, Object> attributes = new HashMap<String, Object>();
	private String phoneNumber;
	private String extension;


	public VoiceNotifyServiceImpl(MessagingSession messagingSession) throws HornetQException {
		this.messagingSession = messagingSession;
		producer = messagingSession.createProducer(Messaging.NOTIFY_VOICE_REQUEST);
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
	public void setExtension(String extension) {
		this.extension = extension;
	}


	@Override
	public void send() throws HornetQException, IOException {
		ClientMessage message = messagingSession.createMessage(true);

		VoiceRequestMessage request = new VoiceRequestMessage(phoneNumber, extension, template);
		request.addAttributes(attributes);

		message.getBodyBuffer().writeBytes(request.toBytes());
		producer.send(message);

		reset();
	}

}
