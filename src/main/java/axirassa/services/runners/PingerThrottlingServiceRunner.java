
package axirassa.services.runners;

import org.hornetq.api.core.client.ClientSession;

import axirassa.services.Service;
import axirassa.services.pinger.BandwidthMeasurer;
import axirassa.services.pinger.BandwidthThreadAllocator;
import axirassa.services.pinger.PingerThrottlingService;
import axirassa.util.MessagingTools;

public class PingerThrottlingServiceRunner {
	public static void main(String[] args) throws Throwable {
		// 50 threads base; 2000 max; we want 10megabytes/second
		BandwidthThreadAllocator threadAllocator = new BandwidthThreadAllocator(50, 2000, 50, 10000000);
		BandwidthMeasurer bandwidthMeasurer = new BandwidthMeasurer(60000);

		ClientSession session = MessagingTools.getEmbeddedSession();
		Service service = new PingerThrottlingService(session, bandwidthMeasurer, threadAllocator);
		service.execute();
	}
}
