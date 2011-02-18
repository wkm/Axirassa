
package axirassa.webapp.ajax.util;

import java.util.ArrayList;

import javax.servlet.ServletException;

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


	public AxirassaAjaxServlet() {
		super();
	}


	@Override
	public void init() throws ServletException {
		final BayeuxServerImpl server = getBayeux();
		getServletContext().setAttribute(BayeuxServer.ATTRIBUTE, server);

		addStreamingTransport(server);

		super.init();

		new Monitor(server);
		try {
			new TimeService(server);
			new PingerStreamingService(server);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.err.println(server.dump());
		System.err.println(server.getCurrentTransport());
	}


	private void addStreamingTransport(BayeuxServerImpl server) {
		server.addTransport(new HttpStreamingTransport(server, HttpStreamingTransport.PREFIX));

		ArrayList<String> allowedTransports = new ArrayList<String>(server.getAllowedTransports());
		allowedTransports.add(0, HttpStreamingTransport.PREFIX);
		server.setAllowedTransports(allowedTransports);

		server.getLogger().info("KNOWN TRANSPORTS: " + server.getKnownTransportNames());
		server.getLogger().info("ALLOWED TRANSPORTS: " + server.getAllowedTransports());
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
			System.out.println("DATA: " + message.getDataAsMap());
		}


		public void monitorAll(ServerSession session, ServerMessage message) {
			System.out.println("BLAST: " + message.toString());
		}
	}
}
