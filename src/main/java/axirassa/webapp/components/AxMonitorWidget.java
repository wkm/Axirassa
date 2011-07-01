package axirassa.webapp.components;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import axirassa.dao.PingerDAO;
import axirassa.model.PingerEntity;

@Import(stylesheet = { "context:/css/axwidget.css" }, library = {
        "context:/js/dygraph-combined.js", "context:/js/axwidget.js" })
public class AxMonitorWidget {
	@Inject
	private JavaScriptSupport jssupport;

	@Inject
	private ComponentResources resources;

	@Inject
	private PingerDAO pingerDAO;

	@Parameter(required = true, defaultPrefix = "literal")
	@Property
	private Long pingerId;

	@Parameter
	@Property
	private String clientId;

	@Property
	private String pingerName;

	@Property
	private PingerEntity pinger;


	void setupRender() {
		pinger = pingerDAO.findPingerById(pingerId);
		clientId = jssupport.allocateClientId(resources);

		// temporary hack to fix
		pingerName = pinger.getUrl().replace("http://", "");

		jssupport.addScript("AxWidget(%d)", pingerId);
	}
}
