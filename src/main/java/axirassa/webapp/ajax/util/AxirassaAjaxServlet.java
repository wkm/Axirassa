
package axirassa.webapp.ajax.util;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.cometd.bayeux.Message;
import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.bayeux.server.ServerMessage;
import org.cometd.bayeux.server.ServerSession;
import org.cometd.server.AbstractService;
import org.cometd.server.BayeuxServerImpl;

import axirassa.webapp.ajax.TimeService;

public class AxirassaAjaxServlet extends GenericServlet {
	private static final long serialVersionUID = -4248590754241578096L;


	public AxirassaAjaxServlet() {
	}


	@Override
	public void init() throws ServletException {
		super.init();
		final BayeuxServerImpl server = (BayeuxServerImpl) getServletContext().getAttribute(BayeuxServer.ATTRIBUTE);

		new Monitor(server);
		try {
			new TimeService(server);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.err.println(server.dump());
	}





	public static class Monitor extends AbstractService {
		public Monitor(BayeuxServer server) {
			super(server, "monitor");
			addService("/meta/subscribe", "monitorSubscribe");
			addService("/meta/unsubscribe", "monitorUnsubscribe");
			addService("/meta/*", "monitorMeta");

			addService("/*", "monitorAll");
		}


		public void monitorSubscribe(ServerSession session, ServerMessage message) {
			System.out.println("Subscribe from " + session + " for " + message.get(Message.SUBSCRIPTION_FIELD));
		}


		public void monitorUnsubscribe(ServerSession session, ServerMessage message) {
			System.out.println("Unsubscribe from " + session + " for " + message.get(Message.SUBSCRIPTION_FIELD));
		}


		public void monitorMeta(ServerSession session, ServerMessage message) {
			// if (Log.isDebugEnabled())
			System.out.println("META: " + message.toString());
		}


		public void monitorAll(ServerSession session, ServerMessage message) {
			System.out.println("BLAST: " + message.toString());
		}
	}


	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		((HttpServletResponse) res).sendError(503);
	}

}
