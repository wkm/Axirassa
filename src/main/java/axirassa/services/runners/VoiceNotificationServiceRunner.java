
package axirassa.services.runners;

import org.hornetq.api.core.client.ClientSession;

import axirassa.services.Service;
import axirassa.services.phone.VoiceNotificationService;
import axirassa.util.MessagingTools;

public class VoiceNotificationServiceRunner {
	public static void main(String[] args) throws Throwable {
		ClientSession session = MessagingTools.getEmbeddedSession();
		Service service = new VoiceNotificationService(session);

		service.execute();
	}
}
