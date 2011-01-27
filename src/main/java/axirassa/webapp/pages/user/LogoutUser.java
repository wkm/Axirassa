
package axirassa.webapp.pages.user;

import org.apache.tapestry5.annotations.Secure;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.tynamo.security.services.SecurityService;

@Secure
public class LogoutUser {
	@Inject
	private SecurityService security;


	String onActivate() {
		security.getSubject().logout();
		return "Index";
	}
}
