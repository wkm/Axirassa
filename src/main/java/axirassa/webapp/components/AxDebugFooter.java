
package axirassa.webapp.components;

import java.util.List;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

public class AxDebugFooter {
	@Inject
	@Property
	private Request request;

	@Property
	private List<String> headers;

	@Property
	private String header;


	public void setupRender() {
		System.out.println("setting headers: " + request.getHeaderNames());
		headers = request.getHeaderNames();
	}
}
