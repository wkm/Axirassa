
package axirassa.webapp.pages.user;

import org.apache.tapestry5.annotations.Secure;
import org.apache.tapestry5.ioc.annotations.Inject;

import axirassa.webapp.services.AxirassaSecurityService;

@Secure
public class LogoutUser {
	@Inject
	private AxirassaSecurityService security;


	String onActivate() {
		security.getSubject().logout();
		return "Index";
	}
}
