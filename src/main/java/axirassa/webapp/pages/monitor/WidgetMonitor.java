
package axirassa.webapp.pages.monitor;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.tynamo.security.services.SecurityService;

import axirassa.model.PingerEntity;

@RequiresUser
@Import(stylesheet = { "context:/css/axwidget.css" }, library = { "${dwr.js}/engine.js" })
public class WidgetMonitor {
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
