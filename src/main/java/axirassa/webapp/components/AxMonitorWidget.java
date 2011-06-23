
package axirassa.webapp.components;

import java.io.IOException;
import java.util.List;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.hibernate.Session;

import axirassa.dao.PingerDAO;
import axirassa.model.HttpStatisticsEntity;
import axirassa.model.PingerEntity;
import axirassa.util.JSONResponse;
import axirassa.webapp.services.AxirassaSecurityService;
import axirassa.webapp.services.exceptions.AxirassaSecurityException;

@Import(
        stylesheet = { "context:/css/axwidget.css" },
        library = { "context:/js/dojo/dojo.js", "context:/js/axwidget.js" })
public class AxMonitorWidget {

	@Inject
	private Session session;

	@Inject
	private JavaScriptSupport jssupport;

	@Inject
	private PingerDAO pingerDAO;

	@Inject
	private AxirassaSecurityService security;

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
		jssupport.addScript("AxPlot(%d)", pingerId);

		pinger = pingerDAO.findPingerById(pingerId);
		if (pinger == null) {
			System.err.println("UNKNOWN PINGER ID");
			return;
		}

		// temporary hack to fix
		pingerName = pinger.getUrl().replace("http://", "");
	}


	public JSONResponse onAction(long pingerId) throws AxirassaSecurityException, IOException {
		System.err.println("ATTEMPTING TO HANDLE ACTION");

		pinger = pingerDAO.findPingerById(pingerId);
		security.verifyOwnership(pinger);

		List<HttpStatisticsEntity> dataPoints = pingerDAO.getDataPoints(pinger, PingerDAO.SIX_HOURS);

		JSONArray array = new JSONArray();

		for (HttpStatisticsEntity datum : dataPoints) {
			JSONArray line = new JSONArray(datum.getTimestampInMillis(), datum.getResponseTime(),
			        datum.getResponseSize());
			array.put(line);
		}

		return new JSONResponse(array);
	}


	public void setPlotId(String plotId) {
		this.plotId = plotId;
	}


	public String getPlotId() {
		return pingerId.toString();
	}

}
