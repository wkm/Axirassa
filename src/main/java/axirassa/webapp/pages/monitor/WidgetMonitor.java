
package axirassa.webapp.pages.monitor;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.hibernate.Session;

import axirassa.model.PingerEntity;
import axirassa.webapp.services.AxirassaSecurityService;
import axirassa.webapp.services.exceptions.AxirassaSecurityException;

@RequiresUser
@Import(stylesheet = { "context:/css/axwidget.css", "context:/css/axplot.css" }, library = {
        "context:/js/dojo/dojo.js", "${tapestry.scriptaculous}/prototype.js", "context:js/flotr.debug-0.2.0-alpha.js",
        "context:/js/lib/canvas2image.js", "context:js/lib/canvastext.js", "context:/js/ajax.js",
        "context:/js/axplot.js", "context:/js/cometd/cometd-namespace.js", "context:/js/cometd/Utils.js",
        "context:/js/cometd/cometd-json.js", "context:/js/cometd/TransportRegistry.js",
        "context:/js/cometd/RequestTransport.js", "context:/js/cometd/Transport.js",
        "context:/js/cometd/LongPollingTransport.js", "context:/js/cometd/CallbackPollingTransport.js",
        "context:/js/cometd/WebSocketTransport.js", "context:/js/cometd/Cometd.js", "context:/js/cometd/dojocometd.js",
        "context:/js/cometd/HttpStreamingTransport.js", "context:/js/cometd/dojox_HttpStreamingTransport.js",
        "context:/js/axwidget.js" })
public class WidgetMonitor {
	@Inject
	private JavaScriptSupport jssupport;


	void setupRender() {
		jssupport.addScript("axplot = new AxPlot('%s', [[10,20],[20,30],[30,10]])", "plot");
	}


	@Inject
	private Session session;

	@Inject
	private AxirassaSecurityService security;

	private PingerEntity pinger;

	@Property
	private Long id;

	@Property
	private String pingerName;

	@Property
	private final double responseTime = 32;

	@Property
	private final double responseSize = 25;


	public Object onActivate(Long id) throws AxirassaSecurityException {
		if (id == null)
			return false;

		this.id = id;

		PingerEntity entity = PingerEntity.findPingerById(session, id);
		security.verifyOwnership(entity);
		if (entity == null)
			return "Index";

		setPinger(entity);

		return true;
	}


	public void beginRender() {
		jssupport.addScript("PingerLevelDataStream.subscribe(%d, null)", id);
	}


	private void setPinger(PingerEntity pingerEntity) {
		pinger = pingerEntity;
		pingerName = pinger.getUrl().replace("http://", "");
	}

}
