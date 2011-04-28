
package axirassa.services.pinger;

import axirassa.services.AxirassaServiceException
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;

import axirassa.services.exceptions.ExecutedWithoutInstrumentationException;
import axirassa.services.exceptions.PingerServiceException;

class ExecutedWithoutInstrumentationException 
	extends AxirassaServiceException("InstrumentedHttpClient executed without instrumentation")

class InstrumentedHttpClient extends DefaultHttpClient {

	private static final long NANOS_PER_MILLI = 1000000;

	private long startTick;
	private long latencyTick;
	private long responseTick;

	private boolean isInstrumented;
	private boolean isStatisticInvalid;

	private String responseContent;


	public InstrumentedHttpClient() {
		addRequestInterceptor(new HttpRequestInterceptor() {
			@Override
			public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
				if (!isInstrumented)
					isStatisticInvalid = true;

				if (startTick == 0)
					startTick = System.nanoTime();
			}
		});

		addResponseInterceptor(new HttpResponseInterceptor() {
			@Override
			public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
				if (!isInstrumented)
					isStatisticInvalid = true;

				if (latencyTick == 0)
					latencyTick = System.nanoTime();
			}
		});
	}


	private void resetTimings() {
		startTick = 0;
		latencyTick = 0;
		responseTick = 0;
	}


	public HttpResponse executeWithInstrumentation(HttpUriRequest request) throws ClientProtocolException, IOException {
		try {
			isInstrumented = true;
			isStatisticInvalid = false;
			resetTimings();

			HttpResponse response = execute(request);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				responseContent = readInputStreamBuffer(entity.getContent());
			}

			responseTick = System.nanoTime();

			return response;
		} finally {
			isInstrumented = false;
		}
	}


	public static String readInputStreamBuffer(InputStream input) throws IOException {
		InputStreamReader reader = new InputStreamReader(input);
		StringBuilder sb = new StringBuilder(input.available());

		char[] buffer = new char[4096];
		int length;
		while ((length = reader.read(buffer)) != -1)
			sb.append(buffer, 0, length);

		reader.close();
		input.close();

		return sb.toString();

	}


	/**
	 * @return total time to receive the complete response, in milliseconds
	 */
	public int getResponseTime() throws PingerServiceException {
		if (isStatisticInvalid)
			throw new ExecutedWithoutInstrumentationException(this);

		return (int) ((responseTick - startTick) / NANOS_PER_MILLI);
	}


	/**
	 * @return time from the sending of the request to the first byte of the
	 *         response, in milliseconds
	 */
	public int getLatency() throws PingerServiceException {
		if (isStatisticInvalid)
			throw new ExecutedWithoutInstrumentationException(this);

		return (int) ((latencyTick - startTick) / NANOS_PER_MILLI);
	}


	public String getResponseContent() {
		return responseContent;
	}

}
