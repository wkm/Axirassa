
package axirassa.webapp.components;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import axirassa.model.PingerEntity;

@Import(stylesheet = { "context:/css/axwidget.css" }, library = "context:/js/axwidget.js")
public class AxMonitorWidget {

	@Inject
	private JavaScriptSupport jssupport;


	void setupRender() {
		jssupport.addScript("new AxPlot('%s', %d)", plotId, pingerId);
	}


	@Parameter(required = true, defaultPrefix = "literal")
	@Property
	private Long pingerId;

	@Parameter
	private String plotId;

	private PingerEntity pinger;

	@Property
	private String pingerName;


	public void setPlotId(String plotId) {
		this.plotId = plotId;
	}


	public String getPlotId() {
		if (plotId == null)
			plotId = "pinger_" + pingerId;

		return plotId;
	}

}
