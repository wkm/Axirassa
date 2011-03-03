
package axirassa.webapp.mixins;

import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.ioc.annotations.Inject;

@Import(stylesheet = { "context:/css/axbutton.css" })
public class AxButton {
	@Inject
	private ComponentResources resources;

	@Inject
	private ClientElement client;


	void beginRender(MarkupWriter writer) {
		System.out.println("before render");
		writer.element("div", "class", "button");
		writer.element("div", "class", "innerbutton");
	}


	void afterRender(MarkupWriter writer) {
		writer.end(); // innerbutton
		writer.end(); // button
	}
}
