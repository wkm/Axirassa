
package axirassa.services.runners;

import org.hornetq.api.core.client.ClientSession;

import axirassa.services.Service;
import axirassa.services.email.EmailService;
import axirassa.util.MessagingTools;

public class EmailServiceRunner {
	public static void main(String[] args) throws Throwable {
		ClientSession session = MessagingTools.getEmbeddedSession();
		Service service = new EmailService(session);

		service.execute();
	}
}
