
package axirassa.webapp.services.internal;

import org.tynamo.security.services.SecurityService;

import axirassa.webapp.services.AxirassaSecurityService;

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

}
