
package test.axirassa.oneoffs;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Router;

import axirassa.model.PingerEntity;
import axirassa.services.pinger.HttpPinger;

public class TestPingerServiceLongevity {
	/**
	 * A test structured to run a single pinger service at maximum speed against
	 * an incredibly lightweight servlet; we're looking for memory leaks, file
	 * handle leaks, etc.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testPingerServiceLongevity() throws Exception {
		PingerLongevityTestService testService = new PingerLongevityTestService();
		testService.start();

		HttpPinger pinger = new HttpPinger();

		PingerEntity entity = new PingerEntity();
		entity.setUrl("http://localhost:8183/");

		for (int i = 0; i < 500000; i++) {
			System.out.printf("ping [%d]\n", i);
			pinger.ping(entity);
		}
	}
}

class PingerLongevityTestService extends ServerResource {
	public static final int PORT_NUMBER = 8183;
	private Component component;


	public void start() throws Exception {
		component = new Component();
		component.getServers().add(Protocol.HTTP, PORT_NUMBER);

		component.getDefaultHost().attach(new PingerLongevityTestApplication());
		component.start();
	}
}

class PingerLongevityTestApplication extends Application {
	@Override
	public synchronized Restlet createInboundRoot() {
		Router router = new Router(getContext());

		Restlet helloRestlet = new HelloRestlet(getContext());
		router.attach("/", helloRestlet);

		return router;
	}
}

@Slf4j
class HelloRestlet extends Restlet {
	public HelloRestlet(Context context) {
		super(context);
	}


	@Override
	public void handle(Request request, Response response) {
		try {
			response.setEntity(new StringRepresentation("Hello"));
		} catch (Exception e) {
			log.error("Exception", e);
		}
	}
}
