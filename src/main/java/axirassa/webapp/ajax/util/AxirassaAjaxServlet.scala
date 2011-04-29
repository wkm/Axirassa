
package axirassa.webapp.ajax.util;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cometd.bayeux.Message;
import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.bayeux.server.ServerMessage;
import org.cometd.bayeux.server.ServerSession;
import org.cometd.server.AbstractService;
import org.cometd.server.BayeuxServerImpl;
import org.cometd.server.CometdServlet;

import axirassa.webapp.ajax.PingerStreamingService;
import axirassa.webapp.ajax.TimeService;
import axirassa.webapp.ajax.httpstream.HttpStreamingTransport;

public class AxirassaAjaxServlet extends CometdServlet {
	private static final long serialVersionUID = -4248590754241578096L;


	public AxirassaAjaxServlet () {
		super();
	}


	@Override
	public void init () throws ServletException {
		super.init();

		new Monitor(getBayeux());
		try {
			new TimeService(getBayeux());
			new PingerStreamingService(getBayeux());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.err.println(getBayeux().dump());
		System.err.println(getBayeux().getCurrentTransport());
	}


	@Override
	protected BayeuxServerImpl newBayeuxServer () {
		BayeuxServerImpl server = new BayeuxServerImpl();

		server.addTransport(new HttpStreamingTransport(server, HttpStreamingTransport.PREFIX));

		ArrayList<String> allowedTransports = new ArrayList<String>(server.getAllowedTransports());
		allowedTransports.add(0, HttpStreamingTransport.PREFIX);
		server.setAllowedTransports(allowedTransports);

		return server;
	}


	@Override
	protected void service (HttpServletRequest request, HttpServletResponse response) throws ServletException,
	        IOException {
		try {
			super.service(request, response);
		} catch (IllegalStateException e) {
			System.err.println("IGNORING EXCEPTION:");
			e.printStackTrace();
		}
	}





	public static class Monitor extends AbstractService {
		public Monitor (BayeuxServer server) {
			super(server, "monitor");
			addService("/meta/subscribe", "monitorSubscribe");
			addService("/meta/unsubscribe", "monitorUnsubscribe");
			addService("/meta/*", "monitorMeta");

			addService("/*", "monitorAll");
		}


		public void monitorSubscribe (ServerSession session, ServerMessage message) {
			System.out.println("AXMONITOR Subscribe from " + session + " for "
			        + message.get(Message.SUBSCRIPTION_FIELD));
		}


		public void monitorUnsubscribe (ServerSession session, ServerMessage message) {
			System.out.println("AXMONITOR Unsubscribe from " + session + " for "
			        + message.get(Message.SUBSCRIPTION_FIELD));
		}


		public void monitorMeta (ServerSession session, ServerMessage message) {
			// if (Log.isDebugEnabled())
			System.out.println("AXMONITOR META: " + message.toString());
			System.out.println("AXMONITOR DATA: " + message.getDataAsMap());
		}


		public void monitorAll (ServerSession session, ServerMessage message) {
			System.out.println("AXMONITOR BLAST: " + message.toString());
		}
	}
}
