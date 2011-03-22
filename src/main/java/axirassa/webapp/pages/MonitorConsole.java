package axirassa.webapp.pages;


import axirassa.dao.UserDAO;
import axirassa.model.PingerEntity;
import axirassa.model.UserEntity;
import axirassa.webapp.services.AxirassaSecurityService;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;


@RequiresUser
public class MonitorConsole {
	@Inject
	private Session session;

	@Inject
	private AxirassaSecurityService security;

	@Inject
	private UserDAO userDAO;

	@Property
	private List<PingerEntity> pingers;

	@Property
	private PingerEntity pinger;


	public void onActivate () {
		UserEntity user = userDAO.getUserByEmail(security.getEmail());
		Query query = session.createQuery("from PingerEntity where user=:user");
		query.setEntity("user", user);

		pingers = query.list();
	}
}
