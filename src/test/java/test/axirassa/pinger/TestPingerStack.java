
package test.axirassa.pinger;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientProducer;
import org.hornetq.api.core.client.ClientSession;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import axirassa.config.Messaging;
import axirassa.model.PingerEntity;
import axirassa.services.pinger.BandwidthMeasurer;
import axirassa.services.pinger.BandwidthThreadAllocator;
import axirassa.services.pinger.HttpPinger;
import axirassa.services.pinger.PingerService;
import axirassa.services.pinger.PingerThrottlingService;
import axirassa.services.util.ServiceRunnableBridge;
import axirassa.util.EmbeddedMessagingServer;
import axirassa.util.MessagingTools;

@Slf4j
public class TestPingerStack {
	private static PingerTestServer server;
	private static Thread throttlingThread;
	private static Thread pinger1Thread;


	@BeforeClass
	public static void startTestServer() throws Exception {
		server = new PingerTestServer();
		server.start();

		EmbeddedMessagingServer.start();

		BandwidthThreadAllocator threadAllocator = new BandwidthThreadAllocator(1, 5, 1, 500000);
		BandwidthMeasurer bandwidthMeasurer = new BandwidthMeasurer(60000);
		ClientSession messaging = MessagingTools.getEmbeddedSession();

		throttlingThread = new Thread(new ServiceRunnableBridge(new PingerThrottlingService(messaging,
		        bandwidthMeasurer, threadAllocator)));
		throttlingThread.start();

		pinger1Thread = new Thread(new ServiceRunnableBridge(new PingerService(MessagingTools.getEmbeddedSession())));
		pinger1Thread.start();
	}


	@AfterClass
	public static void stopTestServer() throws Exception {
		server.stop();
		throttlingThread.interrupt();

		EmbeddedMessagingServer.stop();
	}


	private void warmup() {
		HttpPinger pinger = new HttpPinger();
		PingerEntity entity = new PingerEntity();
		entity.setUrl(PingerTestServer.delayUrl(5));

		for (int i = 0; i < 3; i++)
			pinger.ping(entity);
	}


	private void postPingRequest(ClientSession messaging, ClientProducer producer, String url) throws IOException,
	        HornetQException {
		PingerEntity pinger = new PingerEntity();
		pinger.setUrl(url);

		ClientMessage message = messaging.createMessage(false);
		message.getBodyBuffer().writeBytes(pinger.toBytes());

		producer.send(message);
	}


	private void postPingRequest(ClientSession messaging, ClientProducer producer, int copies, String url) throws IOException, HornetQException {
		for (int i = 0; i < copies; i++)
			postPingRequest(messaging, producer, url);
	}


	@Test
	public void restletTest() throws Exception {
		warmup();

		ClientSession pingerSend = MessagingTools.getEmbeddedSession();
		ClientProducer pingerProducer = pingerSend.createProducer(Messaging.PINGER_REQUEST_QUEUE);

		postPingRequest(pingerSend, pingerProducer, 10, PingerTestServer.bandwidthUrl(50000, 100));
		log.info("Populated 10x 50KB pings on 100ms delay");

		Thread.sleep(60000);

		pingerSend.close();
		pingerProducer.close();
	}
}
