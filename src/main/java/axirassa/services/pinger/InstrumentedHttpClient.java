
package axirassa.services.pinger;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;

public class InstrumentedHttpClient extends DefaultHttpClient {

	private long startTick;
	private long responseTick;
	private long finishTick;


	public InstrumentedHttpClient() {
		addRequestInterceptor(new HttpRequestInterceptor() {
			@Override
			public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
				if (startTick == 0)
					startTick = System.nanoTime();
			}
		});

		addResponseInterceptor(new HttpResponseInterceptor() {
			@Override
			public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
				if (responseTick == 0)
					responseTick = System.nanoTime();
			}
		});
	}


	private void resetTimings() {
		startTick = 0;
		responseTick = 0;
		finishTick = 0;
	}

}
