
package axirassa.webapp.pages.monitor;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Query;
import org.hibernate.Session;

import axirassa.dao.UserDAO;
import axirassa.model.PingerEntity;
import axirassa.model.UserEntity;
import axirassa.webapp.services.AxirassaSecurityService;
import axirassa.webapp.services.exceptions.AxirassaSecurityException;

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


	public void onActivate () throws AxirassaSecurityException {
		UserEntity user = security.getUserEntity();

		Query query = database.createQuery("from PingerEntity where user=:user");
		query.setEntity("user", user);

		pingers = query.list();
	}
}
