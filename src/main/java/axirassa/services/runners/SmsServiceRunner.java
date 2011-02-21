
package axirassa.services.runners;

import axirassa.services.Service;
import axirassa.services.phone.SmsNotificationService;

public class SmsServiceRunner {
	public static void main(String[] args) throws Exception {
		// ClientSession session = MessagingTools.getEmbeddedSession();
		Service service = new SmsNotificationService(null);

		service.execute();
	}
}
