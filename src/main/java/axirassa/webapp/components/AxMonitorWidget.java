
package axirassa.webapp.components;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.hibernate.Session;

import axirassa.model.PingerEntity;

@Import(stylesheet = { "context:/css/axwidget.css" }, library = "context:/js/axwidget.js")
public class AxMonitorWidget {

	@Inject
	private Session session;

	@Inject
	private JavaScriptSupport jssupport;

	@Parameter(required = true, defaultPrefix = "literal")
	@Property
	private Long pingerId;

	@Parameter
	private String plotId;

	@Property
	private PingerEntity pinger;

	@Property
	private String pingerName;


	void setupRender() {
		jssupport.addScript("AxPlot('%s', %d)", getPlotId(), pingerId);

		System.out.println("hunging for pinger");
		pinger = PingerEntity.findPingerById(session, pingerId);
		if (pinger == null) {
			System.err.println("UNKNOWN PINGER ID");
			return;
		}

		pingerName = pinger.getUrl().replace("http://", "");
	}


	public void setPlotId(String plotId) {
		this.plotId = plotId;
	}


	public String getPlotId() {
		if (plotId == null)
			plotId = "pinger_" + pingerId;

		return plotId;
	}

}
