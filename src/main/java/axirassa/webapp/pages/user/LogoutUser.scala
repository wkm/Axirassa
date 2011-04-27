
package axirassa.webapp.pages.user

import org.apache.tapestry5.annotations.Secure
import org.apache.tapestry5.ioc.annotations.Inject

import axirassa.webapp.services.AxirassaSecurityService

@Secure
class LogoutUser {
	@Inject
	var security:  AxirassaSecurityService = _ 


	def onActivate() = {
		security.getSubject.logout()
		"Index"
	}
}
