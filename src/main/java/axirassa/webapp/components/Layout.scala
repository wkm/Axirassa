
package axirassa.webapp.components

import org.apache.shiro.subject.Subject
import org.apache.tapestry5.BindingConstants
import org.apache.tapestry5.annotations.Import
import org.apache.tapestry5.annotations.Parameter
import org.apache.tapestry5.ioc.annotations.Inject

import axirassa.webapp.pages.SecurityViolation
import axirassa.webapp.services.AxirassaSecurityService
import axirassa.webapp.services.AxirassaSecurityException

@Import(stylesheet = Array("context:/css/form.css"))
class Layout {
    @Inject
    var security : AxirassaSecurityService = _

    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    var pageTitle : String = _

    def getPageTitle = pageTitle

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    var header : String = _

    def getHeader = header

    def getUsername() = {
        val subject = security.getSubject

        if (subject.isRemembered() || subject.isAuthenticated())
            security.getEmail
        else null
    }

    def onException(cause : Throwable) = {
        if (cause.isInstanceOf[AxirassaSecurityException])
            classOf[SecurityViolation]
        else
            throw cause
    }
}
