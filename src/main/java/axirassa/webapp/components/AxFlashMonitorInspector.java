
package axirassa.webapp.components;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import axirassa.dao.PingerDAO;
import axirassa.model.PingerEntity;
import axirassa.webapp.pages.monitor.DataMonitor;

@Import(library = { "context:/js/amstock/swfobject.js" })
public class AxFlashMonitorInspector {
	@Inject
	private PageRenderLinkSource linkSource;

	@Inject
	private ComponentResources resources;

	@Inject
	private PingerDAO pingerDAO;

	private PingerEntity pinger;

	@Property
	@Parameter(required = true, defaultPrefix = "literal")
	private Long pingerId;

	@Property
	@Parameter("context:/js/amstock/amstock.swf")
	private String swfObjectPath;

	@Property
	@Parameter("context:/js/amstock/amstock_settings.xml")
	private String settingsPath;

	@Property
	private String dataUrl;

	@Property
	private String clientId;

	@Environmental
	private JavaScriptSupport jssupport;

	@Property
	private String pingerName;


	private void setupRender() {
		Link link = linkSource.createPageRenderLinkWithContext(DataMonitor.class);
		dataUrl = link.toAbsoluteURI() + ":csv/" + pingerId;
		clientId = jssupport.allocateClientId("mon");

		pinger = pingerDAO.findPingerById(pingerId);
		pingerName = pinger.getUrl();
	}
}
