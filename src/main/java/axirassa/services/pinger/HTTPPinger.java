
package axirassa.services.pinger;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;

import axirassa.model.HttpStatisticsEntity;
import axirassa.model.PingerEntity;
import axirassa.services.exceptions.AxirassaServiceException;

public class HTTPPinger {

	private final InstrumentedHttpClient client;


	public HTTPPinger() {
		client = new InstrumentedHttpClient();
	}


	public HttpStatisticsEntity ping(PingerEntity entity) throws ClientProtocolException, IOException,
	        AxirassaServiceException {
		HttpGet get = new HttpGet(entity.getUrl());
		HttpResponse response = client.executeWithInstrumentation(get);

		System.out.println("Latency: " + client.getLatency() + "ms  Response: " + client.getResponseTime()
		        + "ms  URL: " + entity.getUrl());

		HttpStatisticsEntity statistic = new HttpStatisticsEntity();

		statistic.setPinger(entity);
		statistic.setLatency(client.getLatency());
		statistic.setResponseTime(client.getResponseTime());
		statistic.setStatusCode(response.getStatusLine().getStatusCode());
		statistic.setResponseSize(client.getResponseContent().length());
		statistic.setUncompressedSize(0);

		return statistic;
	}


	public static void main(String[] args) throws ClientProtocolException, IOException, AxirassaServiceException {
		HTTPPinger pinger = new HTTPPinger();
		PingerEntity entity = new PingerEntity();
		entity.setUrl("http://google.com");

		pinger.ping(entity);
	}
}
