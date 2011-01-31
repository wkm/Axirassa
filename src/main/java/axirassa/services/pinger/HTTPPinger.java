
package axirassa.services.pinger;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;

import axirassa.messaging.PingerResponseMessage;
import axirassa.services.exceptions.PingerServiceException;

public class HTTPPinger {

	private final InstrumentedHttpClient client;
	private final String url;


	public HTTPPinger(String url) {
		client = new InstrumentedHttpClient();
		this.url = url;
	}


	public PingerResponseMessage ping() throws ClientProtocolException, IOException, PingerServiceException {
		HttpGet get = new HttpGet(url);
		client.executeWithInstrumentation(get);

		System.out.println("++ TIMING  +++++++++++++++++++++++++++++++++++++++++");
		System.out.println("LATENCY: " + client.getLatency() + "ms");
		System.out.println("RESPONSE:   " + client.getResponseTime() + "ms");
		System.out.println("++ CONTENT +++++++++++++++++++++++++++++++++++++++++");
		System.out.println(client.getResponseContent());

		return null;
	}


	public static void main(String[] args) throws ClientProtocolException, IOException, PingerServiceException {
		HTTPPinger pinger = new HTTPPinger("http://localhost:8080/axirassa");
		pinger.ping();
	}
}
