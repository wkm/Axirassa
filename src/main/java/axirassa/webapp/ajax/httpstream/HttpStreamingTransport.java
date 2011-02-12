
package axirassa.webapp.ajax.httpstream;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cometd.bayeux.server.ServerMessage;
import org.cometd.server.BayeuxServerImpl;
import org.cometd.server.transport.HttpTransport;
import org.eclipse.jetty.util.log.Logger;

public class HttpStreamingTransport extends HttpTransport {
	public final static String PREFIX = "http-streaming";


	public HttpStreamingTransport(BayeuxServerImpl bayeux, String name) {
		super(bayeux, name);

		setOptionPrefix(PREFIX);

		info("constructed");
	}


	protected Logger getLogger() {
		return getBayeux().getLogger();
	}


	protected void info(Object... args) {
		if (getLogger().isDebugEnabled()) {
			StringBuffer sb = new StringBuffer("#### HTTP STREAMING: ");
			for (Object arg : args)
				if (arg == null)
					sb.append("null ");
				else
					sb.append(arg.toString()).append(' ');

			getLogger().debug(sb.toString());
		}
	}


	public ServerMessage.Mutable[] parseRequestMessages(HttpServletRequest request) throws IOException, ParseException {
		return parseMessages(request);
	}


	@Override
	public long getMaxInterval() {
		return 60000;
	}


	@Override
	protected void init() {
		super.init();
		info("initializing");
	}


	@Override
	public boolean accept(HttpServletRequest request) {
		return "POST".equals(request.getMethod());
	}


	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		HttpStreamingTransportHandler handler = new HttpStreamingTransportHandler(this, request, response);
		handler.handle();
	}

}
