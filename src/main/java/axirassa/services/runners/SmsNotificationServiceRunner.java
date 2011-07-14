
package axirassa.services.runners;

import org.hornetq.api.core.client.ClientSession;

import axirassa.services.Service;
import axirassa.services.phone.SmsNotificationService;
import axirassa.util.MessagingTools;

public class SmsNotificationServiceRunner {
	public static void main(String[] args) throws Throwable {
		ClientSession session = MessagingTools.getEmbeddedSession();
		Service service = new SmsNotificationService(session);

		service.execute();
	}
}
