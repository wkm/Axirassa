package axirassa.services.runners;

import org.hornetq.api.core.client.ClientSession;

import axirassa.services.Service;
import axirassa.services.pinger.PingerThrottlingService;
import axirassa.util.MessagingTools;

public class PingerThrottlingServiceRunner {
	public static void main(String[] args) throws Throwable {
		ClientSession session = MessagingTools.getEmbeddedSession();
		Service service = new PingerThrottlingService(session);
		service.execute();
	}
}
