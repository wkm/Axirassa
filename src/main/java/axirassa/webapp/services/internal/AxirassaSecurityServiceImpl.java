
package axirassa.webapp.services.internal;

import org.apache.shiro.subject.Subject;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.tynamo.security.services.SecurityService;

import axirassa.dao.UserDAO;
import axirassa.dao.UserEmailAddressDAO;
import axirassa.model.EntityWithUser;
import axirassa.model.UserEmailAddressEntity;
import axirassa.model.UserEntity;
import axirassa.webapp.services.AxirassaSecurityService;
import axirassa.webapp.services.exceptions.AxirassaSecurityException;

public class AxirassaSecurityServiceImpl implements AxirassaSecurityService {

	@Inject
	private SecurityService security;

	@Inject
	private Session database;

	@Inject
	private UserDAO userDAO;

	@Inject
	private UserEmailAddressDAO userEmailAddressDAO;


	@Override
	public boolean isAuthenticated() {
		return security.isAuthenticated();
	}


	@Override
	public boolean isGuest() {
		return security.isGuest();
	}


	@Override
	public boolean isUser() {
		return security.isUser();
	}


	@Override
	public String getEmail() {
		return (String) security.getSubject().getPrincipal();
	}


	@Override
	public Subject getSubject() {
		return security.getSubject();
	}


	@Override
	public void verifyOwnership(EntityWithUser entity) throws AxirassaSecurityException {
		if (entity.getUser() == null)
			throw new AxirassaSecurityException();

		UserEntity user = entity.getUser();
		UserEmailAddressEntity emailEntity = userEmailAddressDAO.getPrimaryEmail(user);

		if (!getEmail().equals(emailEntity.getEmail()))
			throw new AxirassaSecurityException();
	}


	@Override
	public UserEntity getUserEntity() throws AxirassaSecurityException {
		String email = getEmail();
		if (email == null)
			return null;

		UserEntity user = userDAO.getUserByEmail(getEmail());

		if (user == null)
			throw new AxirassaSecurityException();

		return user;
	}

}
