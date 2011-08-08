
package test.axirassa.pinger;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.data.Status;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Router;

import axirassa.util.RandomStringGenerator;

/**
 * A very lightweight server intended for testing pingers.
 * 
 * @author wiktor
 */
public class PingerTestServer extends ServerResource {
	public static final int PORT_NUMBER = 8182;
	public static final String BASE_URL = "http://localhost:" + PORT_NUMBER;
	private Component component;

	public static final int TIMING_ADJUSTMENT = -2;


	public void start() throws Exception {
		component = new Component();
		component.getServers().add(Protocol.HTTP, PORT_NUMBER);

		component.getDefaultHost().attach(new PingerTestApplication());

		component.start();
	}


	public void stop() throws Exception {
		if (component != null)
			component.stop();
	}


	public static void main(String[] args) throws Exception {
		PingerTestServer server = new PingerTestServer();
		server.start();
	}


	public static String delayUrl(int delay) {
		return String.format("%s/delay/%d", BASE_URL, delay);
	}


	public static String sizeUrl(int size) {
		return String.format("%s/size/%d", BASE_URL, size);
	}


	public static String bandwidthUrl(int size, int time) {
		return String.format("%s/bandwidth/%d/%d", BASE_URL, size, time);
	}

}

class PingerTestApplication extends Application {
	@Override
	public synchronized Restlet createInboundRoot() {
		Router router = new Router(getContext());

		router.attach("/status/{code}", new StatusRestlet(getContext()));
		router.attach("/size/{size}", new SizeRestlet(getContext()));
		router.attach("/delay/{delay}", new DelayRestlet(getContext()));
		router.attach("/bandwidth/{size}/{time}", new BandwidthRestlet(getContext()));

		return router;
	}
}

class BandwidthRestlet extends Restlet {
	public BandwidthRestlet(Context context) {
		super(context);
	}


	@Override
	public void handle(Request request, Response response) {
		int size = Integer.parseInt((String) request.getAttributes().get("size"));
		int time = Integer.parseInt((String) request.getAttributes().get("time"));
		long startTick = System.currentTimeMillis();

		String body = RandomStringGenerator.makeRandomStringToken(size);

		long delay = System.currentTimeMillis() - startTick;
		delay = time - delay + PingerTestServer.TIMING_ADJUSTMENT;

		if (delay > 0)
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				// ignore
			}

		response.setEntity(new StringRepresentation(body));
	}
}

class DelayRestlet extends Restlet {
	public DelayRestlet(Context context) {
		super(context);
	}


	@Override
	public void handle(Request request, Response response) {
		int delay = Integer.parseInt((String) request.getAttributes().get("delay"));
		delay += PingerTestServer.TIMING_ADJUSTMENT;

		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			// ignore
		}

		response.setEntity(new StringRepresentation("Delay of " + delay));
	}
}

class SizeRestlet extends Restlet {
	public SizeRestlet(Context context) {
		super(context);
	}


	@Override
	public void handle(Request request, Response response) {
		int size = Integer.parseInt((String) request.getAttributes().get("size"));
		response.setEntity(new StringRepresentation(RandomStringGenerator.makeRandomStringToken(size)));
	}
}

class StatusRestlet extends Restlet {

	public StatusRestlet(Context context) {
		super(context);
	}


	@Override
	public void handle(Request request, Response response) {
		Object codeObject = request.getAttributes().get("code");
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

		response.setStatus(new Status(statuscode));
	}
}
