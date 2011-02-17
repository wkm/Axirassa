
package axirassa.webapp.pages;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Query;
import org.hibernate.Session;
import org.tynamo.security.services.SecurityService;

import axirassa.model.PingerEntity;
import axirassa.model.UserEntity;

@RequiresUser
public class MonitorConsole {
	@Inject
	private Session session;

	@Inject
	private SecurityService security;

	@Property
	private List<PingerEntity> pingers;

	@Property
	private PingerEntity pinger;


	public void onActivate() {
		UserEntity user = UserEntity.getUserByEmail(session, (String) security.getSubject().getPrincipal());
		Query query = session.createQuery("from PingerEntity where user=:user");
		query.setEntity("user", user);

		pingers = query.list();
	}
}
