
package axirassa.webapp.pages.monitor;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Query;
import org.hibernate.Session;

import axirassa.model.MonitorTypeEntity;
import axirassa.model.PingerEntity;
import axirassa.model.UserEntity;
import axirassa.webapp.services.AxirassaSecurityService;

@RequiresUser
public class ListMonitor {
	@Inject
	private Session session;

	@Inject
	private AxirassaSecurityService security;

	@Property
	private List<PingerEntity> pingers;

	@Property
	private PingerEntity pinger;

	@Property
	private MonitorTypeEntity type;


	public void onActivate() {
		UserEntity user = UserEntity.getUserByEmail(session, security.getEmail());

		Query query = session.createQuery("from PingerEntity where user=:user");
		query.setEntity("user", user);

		pingers = query.list();
	}
}
