
package axirassa.webapp.components;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import axirassa.dao.PingerDAO;
import axirassa.model.PingerEntity;
import axirassa.webapp.services.AxirassaSecurityService;

@Import(stylesheet = { "context:/css/axwidget.css" }, library = {
        "context:/js/dygraph-combined.js", "context:/js/axwidget.js" })
public class AxMonitorInspector {
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
	@Property
	private String plotId;

	@Property
	private PingerEntity pinger;

	void setupRender() {
		pinger = pingerDAO.findPingerById(pingerId);
		if (pinger == null) {
			System.err.println("UNKNOWN PINGER ID");
			return;
		}

		jssupport.addScript("AxInspector(%d)", pingerId);
	}
}
