package axirassa.webapp.pages.monitor;


import axirassa.dao.UserDAO;
import axirassa.model.MonitorTypeEntity;
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
public class ListMonitor {
	@Inject
	private Session database;

	@Inject
	private AxirassaSecurityService security;

	@Inject
	private UserDAO userDAO;


	@Property
	private List<PingerEntity> pingers;

	@Property
	private PingerEntity pinger;

	@Property
	private MonitorTypeEntity type;


	public void onActivate () {
		UserEntity user = userDAO.getUserByEmail(security.getEmail());

		Query query = database.createQuery("from PingerEntity where user=:user");
		query.setEntity("user", user);

		pingers = query.list();
	}
}
