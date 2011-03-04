
package axirassa.services.pinger;

import java.io.IOException;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;

import axirassa.model.HttpStatisticsEntity;
import axirassa.model.PingerEntity;
import axirassa.services.exceptions.AxirassaServiceException;

public class HttpPinger {
	private final static String USER_AGENT = "Mozilla/5.0 (axirassa-pinger 0.5; en-us)";

	private final InstrumentedHttpClient client;


	public HttpPinger() {
		client = new InstrumentedHttpClient();
	}


	public HttpStatisticsEntity ping(PingerEntity entity) throws ClientProtocolException, IOException,
	        AxirassaServiceException {

		HttpGet get = new HttpGet(entity.getUrl());
		get.setHeader("User-Agent", USER_AGENT);

		HttpResponse response = client.executeWithInstrumentation(get);

		System.out.println("Latency: " + client.getLatency() + "ms  Response: " + client.getResponseTime()
		        + "ms  URL: " + entity.getUrl());

		HttpStatisticsEntity statistic = new HttpStatisticsEntity();

		statistic.setTimestamp(new Date());
		statistic.setPinger(entity);
		statistic.setLatency(client.getLatency());
		statistic.setResponseTime(client.getResponseTime());
		statistic.setStatusCode(response.getStatusLine().getStatusCode());
		statistic.setResponseSize(client.getResponseContent().length());
		statistic.setUncompressedSize(0);

		return statistic;
	}
}
