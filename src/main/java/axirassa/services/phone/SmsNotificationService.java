
package axirassa.services.phone;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientSession;

import axirassa.config.Messaging;
import axirassa.messaging.SmsRequestMessage;
import axirassa.services.Service;
import axirassa.util.AutoSerializingObject;

public class SmsNotificationService implements Service {
	private final ClientSession messagingSession;
	private final HttpClient httpClient;


	public SmsNotificationService(ClientSession messagingSession) {
		this.messagingSession = messagingSession;
		this.httpClient = new DefaultHttpClient();
	}


	@Override
	public void execute() throws Exception {
		messagingSession.start();

		ClientConsumer consumer = messagingSession.createConsumer(Messaging.NOTIFY_SMS_REQUEST);

		while (true) {
			try {
				ClientMessage message = consumer.receive();

				System.out.println("Received Sms message: " + message);

				byte[] buffer = new byte[message.getBodyBuffer().readableBytes()];
				message.getBodyBuffer().readBytes(buffer);

				Object rawobject = AutoSerializingObject.fromBytes(buffer);

				if (rawobject instanceof SmsRequestMessage) {
					SmsRequestMessage smsRequest = (SmsRequestMessage) rawobject;
					System.out.println("ATTRIBUTES: " + smsRequest.getAttibuteMap());

					String text = PhoneTemplateFactory.instance.getText(smsRequest.getTemplate(),
					                                                    PhoneTemplateType.SMS,
					                                                    smsRequest.getAttibuteMap());

					SendSMS sender = new SendSMS(smsRequest.getPhoneNumber(), text);
					sender.send(httpClient);
				}

				message.acknowledge();
				messagingSession.commit();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
