
package axirassa.webapp.pages.user

import org.apache.shiro.authc.AuthenticationException
import org.apache.shiro.authc.UsernamePasswordToken
import org.apache.shiro.authz.annotation.RequiresGuest
import org.apache.shiro.web.util.WebUtils
import org.apache.tapestry5.annotations.Component
import org.apache.tapestry5.annotations.Log
import org.apache.tapestry5.annotations.Property
import org.apache.tapestry5.annotations.Secure
import org.apache.tapestry5.ioc.annotations.Inject
import org.apache.tapestry5.services.RequestGlobals
import org.apache.tapestry5.services.Response
import org.tynamo.security.services.PageService

import axirassa.webapp.components.AxForm
import axirassa.webapp.pages.MonitorConsole
import axirassa.webapp.services.AxirassaSecurityService

@Secure
@RequiresGuest
class LoginUser {
    @Inject
    @Property
    var security : AxirassaSecurityService = _

    @Inject
    var requestGlobals : RequestGlobals = _

    @Inject
    var pageService : PageService = _

    @Inject
    var response : Response = _

    @Property
    var email : String = _

    @Property
    var password : String = _

    @Property
    var rememberme : Boolean = _

    @Property
    var isLoggedIn : Boolean = _

    @Component
    var form : AxForm = _

    def onActivate() {
        if (security.isUser) {
            email = security.getEmail
            isLoggedIn = true
        }
    }

    @Log
    def onValidateFromForm() {
        if (email == null || password == null)
            return

        val subject = security.getSubject
        try {
            val token = new UsernamePasswordToken(email, password)
            token.setRememberMe(rememberme)
            subject.login(token)
        } catch {
            case e : AuthenticationException => showInvalidLoginMessage()
        }
    }

    private def showInvalidLoginMessage() {
        form.recordError("E-mail, password combination was not found in records")
    }

    @Log
    def onSuccessFromForm() = {
        val savedRequest = WebUtils.getAndClearSavedRequest(requestGlobals.getHTTPServletRequest())

        if (savedRequest == null)
            pageService.getSuccessPage()

        else if (savedRequest.getMethod().equalsIgnoreCase("get"))
            try {
                response.sendRedirect(savedRequest.getRequestUrl())
                null
            } catch {
                case _ => pageService.getSuccessPage()
            }
        else classOf[MonitorConsole]
    }
}
