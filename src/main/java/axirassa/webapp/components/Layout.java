
package axirassa.webapp.components;

import org.apache.shiro.subject.Subject;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.tynamo.security.services.SecurityService;

@Import(stylesheet = { "context:/css/form.css", "context:/css/fonts.css" })
public class Layout {
	@Inject
	private SecurityService security;

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
			return (String) security.getSubject().getPrincipal();

		return null;
	}
}
