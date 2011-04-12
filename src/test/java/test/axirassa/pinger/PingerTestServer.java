
package test.axirassa.pinger;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.data.Status;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Router;

/**
 * A very lightweight server intended for testing pingers.
 * 
 * @author wiktor
 */
public class PingerTestServer extends ServerResource {
	public static final int PORT_NUMBER = 8182;
	private Component component;


	public void start () throws Exception {
		component = new Component();
		component.getServers().add(Protocol.HTTP, PORT_NUMBER);

		component.getDefaultHost().attach(new PingerTestApplication());

		component.start();
	}


	public void stop () throws Exception {
		if (component != null)
			component.stop();
	}


	public static void main (String[] args) throws Exception {
		PingerTestServer server = new PingerTestServer();
		server.start();
	}

}

class PingerTestApplication extends Application {
	@Override
	public synchronized Restlet createInboundRoot () {
		Router router = new Router(getContext());

		Restlet statusRestlet = new StatusRestlet(getContext());
		router.attach("/status/{code}", statusRestlet);

		return router;
	}
}

class StatusRestlet extends Restlet {

	public StatusRestlet (Context context) {
		super(context);
	}


	@Override
	public void handle (Request request, Response response) {
		System.out.println("HANDLING RESPONSE");

		Object codeObject = request.getAttributes().get("code");
		System.out.println("STATUS CODE: " + codeObject);
		int statuscode;

		if (codeObject == null)
			statuscode = 200;
		else if (codeObject instanceof String)
			try {
				statuscode = Integer.parseInt(((String) codeObject));
			} catch (NumberFormatException ex) {
				statuscode = 200;
			}
		else
			statuscode = 200;

		System.out.println("SETTING STATUS: " + statuscode);
		response.setStatus(new Status(statuscode));
		System.out.println("STATUS: " + response.getStatus());
	}
}
