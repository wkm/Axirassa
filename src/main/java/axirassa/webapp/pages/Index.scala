package axirassa.webapp.pages

import axirassa.webapp.services.AxirassaSecurityService
import org.apache.tapestry5.ioc.annotations.Inject
import org.apache.tapestry5.annotations.Import
/**
 * Start page of application axir.
 */
@Import(stylesheet = Array("context:/css/form.css", "context:/css/main.css"))
class Index {
	@Inject
	var security : AxirassaSecurityService = _

	def onActivate() : Object = {
		if (security.isUser())
			return classOf[MonitorConsole]
		else
			return true.asInstanceOf[Object]
	}
}
