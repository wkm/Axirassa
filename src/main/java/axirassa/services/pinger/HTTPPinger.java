
package axirassa.services.pinger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import axirassa.messaging.PingerResponseMessage;

public class HTTPPinger {

	private final DefaultHttpClient client;
	private final String url;
	private long starttick;
	private long responsetick;
	private long finishtick;


	public HTTPPinger(String url) {
		client = new DefaultHttpClient();
		this.url = url;
	}


	public PingerResponseMessage ping() throws ClientProtocolException, IOException {
		HttpGet get = new HttpGet(url);
		HttpResponse response = client.execute(get);
		HttpEntity entity = response.getEntity();

		if (entity != null) {
			StatusLine line = response.getStatusLine();
			Header header = entity.getContentType();

			InputStream input = entity.getContent();
			InputStreamReader reader = new InputStreamReader(input);
			StringBuilder sb = new StringBuilder();

			char[] buffer = new char[4096];
			int length;
			while ((length = reader.read(buffer)) != -1)
				sb.append(buffer, 0, length);
			finishtick = System.nanoTime();

			System.out.println("++ TIMING  +++++++++++++++++++++++++++++++++++++++++");
			System.out.println("RESPONSE: " + (responsetick - starttick) / 1000000L + "ms");
			System.out.println("FINISH:   " + (finishtick - starttick) / 1000000L + "ms");
		}

		return null;
	}


	public static void main(String[] args) throws ClientProtocolException, IOException {
		HTTPPinger pinger = new HTTPPinger("http://google.com/");
		pinger.ping();
	}
}