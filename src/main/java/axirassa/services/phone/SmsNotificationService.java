
package axirassa.services.phone;

import org.apache.xerces.dom3.as.ASObject;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientSession;

import axirassa.messaging.EmailRequestMessage;
import axirassa.services.Service;
import axirassa.util.AutoSerializingObject;

public class SmsNotificationService implements Service {
	private final ClientSession messagingSession;


	public SmsNotificationService(ClientSession messagingSession) {
		this.messagingSession = messagingSession;
	}


	@Override
	public void execute() throws Exception {
		messagingSession.start();
		
		while(true) {
			try {
				ClientMessage message = consume.receive();
				
				System.out.println("Received Sms message: "+message);
				
				byte[] buffer = new byte[message.getBodyBuffer().readableBytes()];
				message.getBodyBuffer().readBytes(buffer);
				
				Object rawobject = AutoSerializingObject.fromBytes(buffer);
				
				if(rawobject instanceof SmsRequestMessage) {
					SmsRequestMessage smsRequest = (SmsRequestMessage) rawobject;
				}
			}
		}
	}
}
