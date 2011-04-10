
package axirassa.services.pinger;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;

import axirassa.model.HttpStatisticsEntity;
import axirassa.model.PingerEntity;
import axirassa.services.exceptions.AxirassaServiceException;
import axirassa.trigger.Trigger;
import axirassa.trigger.UnknownHostTrigger;

public class HttpPinger {
	private final static String USER_AGENT = "Mozilla/5.0 (axirassa-pinger 0.5; en-us)";

	private final InstrumentedHttpClient client;

	private List<Trigger> triggers;


	public HttpPinger () {
		client = new InstrumentedHttpClient();
	}


	public void resetTriggers () {
		triggers = new ArrayList<Trigger>();
	}


	public void addTrigger (Trigger trigger) {
		triggers.add(trigger);
	}


	public HttpStatisticsEntity ping (PingerEntity entity) throws ClientProtocolException, IOException,
	        AxirassaServiceException {

		resetTriggers();

		HttpGet get = new HttpGet(entity.getUrl());
		get.setHeader("User-Agent", USER_AGENT);

		try {
			HttpResponse response = client.executeWithInstrumentation(get);

			HttpStatisticsEntity statistic = new HttpStatisticsEntity();

			statistic.setTimestamp(new Date());
			statistic.setPinger(entity);
			statistic.setLatency(client.getLatency());
			statistic.setResponseTime(client.getResponseTime());
			statistic.setStatusCode(response.getStatusLine().getStatusCode());
			statistic.setResponseSize(client.getResponseContent().length());
			statistic.setUncompressedSize(0);

			System.out.println("Latency: " + client.getLatency() + "ms  Response: " + client.getResponseTime()
			        + "ms  URL: " + entity.getUrl());

			return statistic;
		} catch (UnknownHostException e) {
			addTrigger(new UnknownHostTrigger());
		}

		System.out.println("TRIGGERS: " + triggers);

		return null;
	}


	public static void main (String[] args) throws ClientProtocolException, IOException, AxirassaServiceException {
		String url = "http://whoisatthisfoo.com/";
		System.out.println("Pinger URL: " + url);

		HttpPinger httpPinger = new HttpPinger();

		PingerEntity pinger = new PingerEntity();
		pinger.setUrl(url);

		httpPinger.ping(pinger);
	}
}
