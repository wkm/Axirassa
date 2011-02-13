
package axirassa.services.runners;

import axirassa.services.Service;
import axirassa.services.sms.SmsService;

public class SmsServiceRunner {
	public static void main(String[] args) throws Exception {
		// ClientSession session = MessagingTools.getEmbeddedSession();
		Service service = new SmsService(null);

		service.execute();
	}
}
