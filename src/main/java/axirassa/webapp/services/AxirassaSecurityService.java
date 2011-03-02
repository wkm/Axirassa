
package axirassa.webapp.services;

import org.apache.shiro.subject.Subject;

public interface AxirassaSecurityService {

	public abstract String getEmail();


	public abstract boolean isUser();


	public abstract boolean isGuest();


	public abstract boolean isAuthenticated();


	public abstract Subject getSubject();

}
