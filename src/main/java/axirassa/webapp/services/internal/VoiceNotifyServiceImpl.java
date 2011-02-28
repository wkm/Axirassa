
package axirassa.webapp.services.internal;

import java.io.IOException;
import java.util.HashMap;

import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.client.ClientProducer;

import axirassa.config.Messaging;
import axirassa.services.phone.PhoneTemplate;
import axirassa.webapp.services.MessagingSession;
import axirassa.webapp.services.VoiceNotifyService;

public class VoiceNotifyServiceImpl implements VoiceNotifyService {
	private final MessagingSession messagingSession;
	private final ClientProducer producer;

	private PhoneTemplate template;
	private final HashMap<String, String> attributes = new HashMap<String, String>();
	private String phoneNumber;
	private String extension;


	public VoiceNotifyServiceImpl(MessagingSession messagingSession) throws HornetQException {
		this.messagingSession = messagingSession;
		producer = messagingSession.createProducer(Messaging.NOTIFY_SMS_REQUEST);
	}


	@Override
	public void startMessage(PhoneTemplate template) {
		attributes.clear();
		this.template = template;
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

	}

}
