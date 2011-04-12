
package axirassa.services.pinger;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;

import axirassa.model.HttpStatisticsEntity;
import axirassa.model.PingerEntity;
import axirassa.services.exceptions.AxirassaServiceException;
import axirassa.trigger.PingerErrorTrigger;
import axirassa.trigger.ProtocolErrorTrigger;
import axirassa.trigger.StatusCodeTrigger;
import axirassa.trigger.Trigger;
import axirassa.trigger.UnknownHostTrigger;

public class HttpPinger {
	private final static String USER_AGENT = "Mozilla/5.0 (axirassa-pinger 0.5; en-us)";

	private final InstrumentedHttpClient client;

	private HashMap<Class<? extends Trigger>, Trigger> triggers;


	public HttpPinger () {
		client = new InstrumentedHttpClient();
	}


	public void resetTriggers () {
		triggers = new LinkedHashMap<Class<? extends Trigger>, Trigger>();
	}


	public void addTrigger (Trigger trigger) {
		triggers.put(trigger.getClass(), trigger);
	}


	public Collection<Trigger> getTriggers () {
		return triggers.values();
	}


	public <T> T getTrigger (Class<? extends T> classObject) {
		return (T) triggers.get(classObject);
	}


	public HttpStatisticsEntity ping (PingerEntity entity) throws ClientProtocolException, IOException,
	        AxirassaServiceException {

		resetTriggers();

		HttpGet get = new HttpGet(entity.getUrl());
		get.setHeader("User-Agent", USER_AGENT);

		HttpStatisticsEntity statistic = null;

		try {
			HttpResponse response = client.executeWithInstrumentation(get);

			statistic = new HttpStatisticsEntity();
			statistic.setTimestamp(new Date());
			statistic.setPinger(entity);
			statistic.setLatency(client.getLatency());
			statistic.setResponseTime(client.getResponseTime());
			statistic.setStatusCode(response.getStatusLine().getStatusCode());
			statistic.setResponseSize(client.getResponseContent().length());
			statistic.setUncompressedSize(0);

			System.out.printf("Latency: %dms Response: %dms Status: [%d] URL: %s\n", statistic.getLatency(),
			                  statistic.getResponseTime(), statistic.getStatusCode(), statistic.getPinger().getUrl());

			addTrigger(new StatusCodeTrigger(statistic.getStatusCode()));
		} catch (UnknownHostException e) {
			addTrigger(new UnknownHostTrigger());
		} catch (ClientProtocolException e) {
			addTrigger(new ProtocolErrorTrigger(e));
		} catch (Exception e) {
			addTrigger(new PingerErrorTrigger(e));
		}

		System.out.println("TRIGGERS: " + triggers);

		return statistic;
	}


	public static void main (String[] args) throws ClientProtocolException, IOException, AxirassaServiceException {
		String url = "http://zanoccio.com/WhoIsAtThisFoo";
		System.out.println("Pinger URL: " + url);

		HttpPinger httpPinger = new HttpPinger();

		PingerEntity pinger = new PingerEntity();
		pinger.setUrl(url);

		httpPinger.ping(pinger);
	}
}
