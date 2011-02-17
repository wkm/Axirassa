
package axirassa.webapp.pages;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.tynamo.security.services.SecurityService;

/**
 * Start page of application axir.
 */
@Import(stylesheet = { "context:/css/fonts.css", "context:/css/form.css", "context:/css/main.css" })
public class Index {
	@Inject
	private SecurityService security;


	Object onActivate() {
		if (security.isUser())
			return "MonitorConsole";
		else
			return true;
	}
}
