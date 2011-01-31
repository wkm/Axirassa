
package axirassa.services.runners;

import org.hornetq.api.core.client.ClientSession;

import axirassa.services.Service;
import axirassa.services.pinger.PingerService;
import axirassa.util.MessagingTools;

public class PingerServiceRunner {
	public static void main(String[] args) throws Exception {
		ClientSession msgsession = MessagingTools.getEmbeddedSession();
		Service service = new PingerService(msgsession);

		service.execute();
	}
}
