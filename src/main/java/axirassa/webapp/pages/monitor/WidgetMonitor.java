
package axirassa.webapp.pages.monitor;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.hibernate.Session;
import org.tynamo.security.services.SecurityService;

import axirassa.model.PingerEntity;

@RequiresUser
@Import(stylesheet = { "context:/css/axwidget.css" }, library = {
        "${tapestry.scriptaculous}/prototype.js", "context:js/flotr.debug-0.2.0-alpha.js",
        "context:js/lib/canvas2image.js", "context:js/lib/canvastext.js", "context:/js/ajax.js",
        "context:/js/axplot.js" })
public class WidgetMonitor {
	@Inject
	private JavaScriptSupport jssupport;


	void setupRender() {
		final String prefix = "http://localhost:8080/dwr";
		jssupport.importJavaScriptLibrary(prefix + "/interface/TextChat.js");
		jssupport.importJavaScriptLibrary(prefix + "/engine.js");
		jssupport.importJavaScriptLibrary(prefix + "/util.js");

		jssupport.addScript("axplot = new AxPlot('%s', [[10,20],[20,30],[30,10]])", "plot");
	}


	@Inject
	private Session session;

	@Inject
	private SecurityService security;

	private PingerEntity pinger;

	@Property
	private Long id;

	@Property
	private String pingerName;

	@Property
	private final double responseTime = 32;

	@Property
	private final double responseSize = 25;


	public Object onActivate(Long id) {
		if (id == null)
			return false;

		this.id = id;

		PingerEntity entity = PingerEntity.findPingerById(session, id);
		if (entity == null)
			return "Index";

		setPinger(entity);

		String username = (String) security.getSubject().getPrincipal();

		if (!pinger.getUser().getEMail().equals(username))
			return "Index";

		return true;
	}


	private void setPinger(PingerEntity pingerEntity) {
		pinger = pingerEntity;
		pingerName = pinger.getUrl().replace("http://", "");
	}

}
