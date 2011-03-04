
package axirassa.webapp.services.internal;

import org.apache.shiro.subject.Subject;
import org.tynamo.security.services.SecurityService;

import axirassa.model.EntityWithUser;
import axirassa.webapp.services.AxirassaSecurityService;
import axirassa.webapp.services.exceptions.AxirassaSecurityException;

public class AxirassaSecurityServiceImpl implements AxirassaSecurityService {

	private final SecurityService security;


	public AxirassaSecurityServiceImpl(SecurityService security) {
		this.security = security;
	}


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

		if (!getEmail().equals(entity.getUser().getEmail()))
			throw new AxirassaSecurityException();
	}

}
