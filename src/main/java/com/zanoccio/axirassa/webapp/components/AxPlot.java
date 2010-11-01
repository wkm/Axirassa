
package com.zanoccio.axirassa.webapp.components;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import com.zanoccio.axirassa.util.RandomStringGenerator;

@Import(library = {
        "context:js/flotr.debug-0.2.0-alpha.js", "context:js/sentinel.js", "context:js/lib/canvas2image.js",
        "context:js/lib/canvastext.js" }, stylesheet = "context:css/axcomponents.css")
public class AxPlot {

	@Environmental
	private JavaScriptSupport javascriptsupport;

	@Inject
	private ComponentResources resources;


	void setupRender() {
		// javascriptsupport.addInitializerCall("plotchart", getID());

		javascriptsupport.addScript("ax.agentcontrol('%s', '%s')", getID(), getSource());
	}


	public static String generateID() {
		return "plot_" + RandomStringGenerator.getInstance().randomString(5);
	}


	/**
	 * An optional unique identifier for this component on the page. If not
	 * specified one is randomly generated.
	 */
	@Parameter(defaultPrefix = "literal")
	private String id;


	public String getID() {
		if (id == null)
			id = generateID();

		return id;
	}


	/**
	 * The data source for this component. By default it's the event handler for
	 * the action specified by the tapestry id of this component.
	 */
	@Parameter(defaultPrefix = "literal")
	private String source;


	public String getSource() {
		if (source == null)
			source = resources.createEventLink("action").toAbsoluteURI();

		return source;
	}


	// detailsHeader
	@Parameter(defaultPrefix = "literal")
	private String detailsHeader;


	public String getDetailsHeader() {
		if (detailsHeader == null)
			detailsHeader = "";

		return detailsHeader;
	}

}
