
package axirassa.webapp.pages;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.ioc.annotations.Inject;

import axirassa.webapp.services.AxirassaSecurityService;

/**
 * Start page of application axir.
 */
@Import(stylesheet = { "context:/css/form.css", "context:/css/main.css" })
public class Index {
	@Inject
	private AxirassaSecurityService security;


	Object onActivate() {
		if (security.isUser())
			return MonitorConsole.class;
		else
			return true;
	}
}
