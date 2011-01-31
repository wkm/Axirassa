
package axirassa.services.pinger;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;

import axirassa.messaging.PingerResponseMessage;
import axirassa.services.exceptions.AxirassaServiceException;

public class HTTPPinger {

	private final InstrumentedHttpClient client;


	public HTTPPinger() {
		client = new InstrumentedHttpClient();
	}


	public PingerResponseMessage ping(String url) throws ClientProtocolException, IOException, AxirassaServiceException {
		System.out.println("HTTPPinger: " + url);
		HttpGet get = new HttpGet(url);
		HttpResponse response = client.executeWithInstrumentation(get);

		System.out.println("++ TIMING  +++++++++++++++++++++++++++++++++++++++++");
		System.out.println("LATENCY: " + client.getLatency() + "ms");
		System.out.println("RESPONSE:   " + client.getResponseTime() + "ms");
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++");

		PingerResponseMessage message = new PingerResponseMessage();
		message.setUrl(url);
		message.setLatencyMillis(client.getLatency());
		message.setResponseTimeMillis(client.getResponseTime());
		message.setStatusCode(response.getStatusLine().getStatusCode());
		message.setResponseSizeBytes(client.getResponseContent().length());
		message.setUncompressedSizeBytes(0);

		return message;
	}


	public static void main(String[] args) throws ClientProtocolException, IOException, AxirassaServiceException {
		HTTPPinger pinger = new HTTPPinger();
		pinger.ping("http://localhost:8080/axirassa");
	}
}
