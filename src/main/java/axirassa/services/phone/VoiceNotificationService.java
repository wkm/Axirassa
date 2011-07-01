
package axirassa.services.phone;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientSession;

import axirassa.config.Messaging;
import axirassa.messaging.VoiceRequestMessage;
import axirassa.services.Service;
import axirassa.util.AutoSerializingObject;

public class VoiceNotificationService implements Service {
	private final ClientSession messaging;
	private final HttpClient httpClient;


	public VoiceNotificationService(ClientSession messagingSession) {
		this.messaging = messagingSession;
		this.httpClient = new DefaultHttpClient();
	}


	@Override
	public void execute() throws Exception {
		messaging.start();

		ClientConsumer consumer = messaging.createConsumer(Messaging.NOTIFY_VOICE_REQUEST);
		while (true) {
			try {
				ClientMessage message = consumer.receive();
				System.out.println("####\n####\n####\n####\n####\n####\n");
				System.out.println("RECIEVED MESSAGE: " + message);

				byte[] buffer = new byte[message.getBodyBuffer().readableBytes()];
				message.getBodyBuffer().readBytes(buffer);

				Object rawobject = AutoSerializingObject.fromBytes(buffer);
				if (rawobject instanceof VoiceRequestMessage) {
					VoiceRequestMessage voiceRequest = (VoiceRequestMessage) rawobject;
					String text = PhoneTemplateFactory.instance.getText(voiceRequest.getTemplate(),
					                                                    PhoneTemplateType.VOICE,
					                                                    voiceRequest.getAttributeMap());

					SendVoice sender = new SendVoice(voiceRequest.getPhoneNumber(), voiceRequest.getExtension(), text);
					sender.send(httpClient);
				}

				message.acknowledge();
				messaging.commit();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
