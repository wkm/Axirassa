
package com.zanoccio.axirassa.pinger;

import java.io.IOException;
import java.net.Socket;

import javax.jms.JMSException;
import javax.naming.NamingException;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpClientConnection;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestExpectContinue;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.qpid.AMQException;
import org.apache.qpid.url.URLSyntaxException;

/**
 * The HTTPPinger provides verification of a single arbitrary URL.
 * 
 * @author wiktor
 */
public class HTTPPinger extends AbstractPinger {

	public void run() throws URLSyntaxException, AMQException, JMSException, NamingException, IOException,
	        HttpException {

		System.out.println("Starting");

		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, "UTF-8");
		HttpProtocolParams.setUserAgent(params, "ax_pinger/0.2");
		HttpProtocolParams.setUseExpectContinue(params, true);

		BasicHttpProcessor httpproc = new BasicHttpProcessor();
		httpproc.addInterceptor(new RequestContent());
		httpproc.addInterceptor(new RequestTargetHost());
		httpproc.addInterceptor(new RequestConnControl());
		httpproc.addInterceptor(new RequestUserAgent());
		httpproc.addInterceptor(new RequestExpectContinue());

		System.out.println("Executing");
		HttpRequestExecutor httpexecutor = new HttpRequestExecutor();

		HttpContext context = new BasicHttpContext();
		HttpHost host = new HttpHost("zanoccio.com", 80);

		DefaultHttpClientConnection conn = new DefaultHttpClientConnection();
		ConnectionReuseStrategy connStrategy = new DefaultConnectionReuseStrategy();

		System.out.println("Continuing");

		context.setAttribute(ExecutionContext.HTTP_CONNECTION, conn);
		context.setAttribute(ExecutionContext.HTTP_TARGET_HOST, host);

		try {

			String[] targets = { "/", "/CSS/main.css" };
			for (String target : targets) {
				if (!conn.isOpen()) {
					System.out.println("Opening a socket");
					Socket socket = new Socket(host.getHostName(), host.getPort());
					conn.bind(socket, params);
					System.out.println("\t... done.");
				}

				BasicHttpRequest request = new BasicHttpRequest("GET", target);
				System.out.println("++++++++++++");
				System.out.println("Request URI: " + request.getRequestLine().getUri());

				request.setParams(params);
				httpexecutor.preProcess(request, httpproc, context);

				System.out.println("Executing...");
				HttpResponse response = httpexecutor.execute(request, conn, context);
				response.setParams(params);
				httpexecutor.postProcess(response, httpproc, context);

				System.out.println("Response: " + response.getStatusLine());

				if (!connStrategy.keepAlive(response, context)) {
					conn.close();
				} else {
					System.out.println("Connection kept alive...");
				}
			}

		} finally {
			conn.close();
		}
	}


	public static void main(String[] args) throws URLSyntaxException, AMQException, JMSException, NamingException,
	        IOException, HttpException {
		HTTPPinger pinger = new HTTPPinger();
		pinger.run();
	}
}
