
package axirassa.webapp.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import axirassa.util.RandomStringGenerator;

@Import(
        library = { "context:js/axplot.js", "context:js/lib/canvastext.js", "context:js/lib/canvas2image.js" },
        stylesheet = "context:css/axplot.css")
public class AxSimplePlot {
	@Environmental
	private JavaScriptSupport javascriptsupport;

	@Inject
	private ComponentResources resources;


	void setupRender() {
		javascriptsupport.addScript("AxPlot.plot('%s')", getId());
	}


	public static String generateId() {
		return "plot_" + RandomStringGenerator.getInstance().randomString(15);
	}


	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String id;


	public String getId() {
		if (id == null)
			id = generateId();

		return id;
	}


	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String source;


	public String getSource() {
		if (source == null)
			source = resources.createEventLink("getPlotData").toAbsoluteURI();

		return source;
	}
}
