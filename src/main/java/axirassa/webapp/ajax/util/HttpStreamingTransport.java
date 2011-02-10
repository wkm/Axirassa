
package axirassa.webapp.ajax.util;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cometd.server.BayeuxServerImpl;
import org.cometd.server.transport.HttpTransport;
import org.eclipse.jetty.util.log.Logger;

public class HttpStreamingTransport extends HttpTransport {
	public final static String PREFIX = "http-streaming";

	private final ConcurrentHashMap<String, AtomicInteger> sessionMap = new ConcurrentHashMap<String, AtomicInteger>();


	protected HttpStreamingTransport(BayeuxServerImpl bayeux, String name) {
		super(bayeux, name);

		setOptionPrefix(PREFIX);

		info("constructed");
	}


	protected Logger getLogger() {
		return getBayeux().getLogger();
	}


	protected void info(Object... args) {
		StringBuffer sb = new StringBuffer("#### HTTP STREAMING: ");
		for (Object arg : args)
			sb.append(arg.toString()).append(' ');

		getLogger().info(sb.toString());
	}


	@Override
	protected void init() {
		info("initializing");
	}


	@Override
	public boolean accept(HttpServletRequest request) {
		info("accepting request", request);
		return false;
	}


	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		info("handling request", request, response);
	}
}
