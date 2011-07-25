
package test.axirassa.pinger;

import lombok.extern.slf4j.Slf4j;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import axirassa.model.PingerEntity;
import axirassa.services.pinger.HttpPinger;
import axirassa.services.pinger.PingerThrottlingService;
import axirassa.services.util.ServiceRunnableBridge;
import axirassa.util.EmbeddedMessagingServer;
import axirassa.util.MessagingTools;

@Slf4j
public class TestPingerStack {
	private static PingerTestServer server;
	private static Thread throttlingThread;


	@BeforeClass
	public static void startTestServer() throws Exception {
		server = new PingerTestServer();
		server.start();

		EmbeddedMessagingServer.start();

		throttlingThread = new Thread(new ServiceRunnableBridge(new PingerThrottlingService(
		        MessagingTools.getEmbeddedSession())));
		throttlingThread.start();
	}


	@AfterClass
	public static void stopTestServer() throws Exception {
		server.stop();

		EmbeddedMessagingServer.stop();
	}


	private void warmup() {
		HttpPinger pinger = new HttpPinger();
		PingerEntity entity = new PingerEntity();
		entity.setUrl(PingerTestServer.delayUrl(5));

		for (int i = 0; i < 3; i++)
			pinger.ping(entity);
	}


	@Test
	public void restletTest() throws Exception {
		warmup();
		throttlingThread.interrupt();
	}
}
