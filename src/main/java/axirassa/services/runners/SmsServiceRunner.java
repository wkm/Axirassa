
package axirassa.services.runners;

import org.hornetq.api.core.client.ClientSession;

import axirassa.services.Service;
import axirassa.services.phone.SmsNotificationService;
import axirassa.util.MessagingTools;

public class SmsServiceRunner {
	public static void main(String[] args) throws Exception {
		ClientSession session = MessagingTools.getEmbeddedSession();
		Service service = new SmsNotificationService(session);

		service.execute();
	}
}
