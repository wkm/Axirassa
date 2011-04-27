
package axirassa.webapp.components;

import org.apache.shiro.subject.Subject;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;

import axirassa.webapp.pages.SecurityViolation;
import axirassa.webapp.services.AxirassaSecurityService;
import axirassa.webapp.services.exceptions.AxirassaSecurityException;

@Import(stylesheet = { "context:/css/form.css" })
public class Layout {
	@Inject
	private AxirassaSecurityService security;

	@Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
	private String pageTitle;


	public String getPageTitle() {
		return pageTitle;
	}


	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String header;


	public String getHeader() {
		return header;
	}


	public String getUsername() {
		Subject subject = security.getSubject();

		if (subject.isRemembered() || subject.isAuthenticated())
			return security.getEmail();

		return null;
	}


	public Object onException(Throwable cause) throws Throwable {
		if (cause instanceof AxirassaSecurityException)
			return SecurityViolation.class;
		else
			throw cause;
	}
}
