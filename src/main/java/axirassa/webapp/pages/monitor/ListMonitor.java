
package axirassa.webapp.pages.monitor;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Query;
import org.hibernate.Session;
import org.tynamo.security.services.SecurityService;

import axirassa.model.MonitorTypeModel;
import axirassa.model.PingerModel;
import axirassa.model.UserModel;

@RequiresUser
public class ListMonitor {
	@Inject
	private Session session;

	@Inject
	private SecurityService security;

	@Property
	private List<PingerModel> pingers;

	@Property
	private PingerModel pinger;

	@Property
	private MonitorTypeModel type;


	public void onActivate() {
		UserModel user = UserModel.getUserByEmail(session, (String) security.getSubject().getPrincipal());

		Query query = session.createQuery("from PingerModel where user=:user");
		query.setEntity("user", user);

		pingers = query.list();
	}
}
