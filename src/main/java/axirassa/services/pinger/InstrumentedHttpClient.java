
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

	private long starttick;
	private long responsetick;
	private long finishtick;


	public InstrumentedHttpClient() {
		addRequestInterceptor(new HttpRequestInterceptor() {
			@Override
			public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
				if (starttick == 0)
					starttick = System.nanoTime();
			}
		});

		addResponseInterceptor(new HttpResponseInterceptor() {
			@Override
			public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
				if (responsetick == 0)
					responsetick = System.nanoTime();
			}
		});
	}


	private void resetTimings() {
		starttick = 0;
		responsetick = 0;
		finishtick = 0;
	}

}
