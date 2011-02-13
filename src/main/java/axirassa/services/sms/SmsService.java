
package axirassa.services.sms;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.hornetq.api.core.client.ClientSession;

import axirassa.services.Service;

public class SmsService implements Service {

	private final ClientSession messagingSession;


	public SmsService(ClientSession messagingSesssion) {
		this.messagingSession = messagingSesssion;
	}


	@Override
	public void execute() throws ClientProtocolException, IOException {
		SendSMS sms = new SendSMS("2175974201",
		        "Axirassa Server Monitor - zanoccio.com is reporting 404s since 1:06am PST");
		sms.send();
	}
}
