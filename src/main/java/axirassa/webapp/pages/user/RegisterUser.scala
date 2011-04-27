
package axirassa.webapp.pages.user

import org.apache.shiro.authz.annotation.RequiresGuest
import org.apache.tapestry5.annotations.Component
import org.apache.tapestry5.annotations.Log
import org.apache.tapestry5.annotations.Property
import org.apache.tapestry5.annotations.Secure
import org.apache.tapestry5.ioc.annotations.Inject
import org.apache.tapestry5.json.JSONObject
import org.apache.tapestry5.services.Request

import axirassa.dao.UserDAO
import axirassa.model.flows.CreateUserFlow
import axirassa.webapp.components.AxForm
import axirassa.webapp.components.AxPasswordField
import axirassa.webapp.components.AxTextField

@Secure
@RequiresGuest
class RegisterUser {
    @Inject
    var request : Request = _

    @Inject
    var userDAO : UserDAO = _

    @Inject
    var createUserFlow : CreateUserFlow = _

    @Property
    var email : String = _

    @Property
    var confirmemail : String = _

    @Property
    var password : String = _

    @Property
    var confirmpassword : String = _

    @Component
    var emailField : AxTextField = _

    @Component
    var confirmEmailField : AxTextField = _

    @Component
    var confirmPasswordField : AxPasswordField = _

    @Component
    var form : AxForm = _

    @Log
    def onAJAXValidateFromEmailField() = {
        val emailvalue = request.getParameter("param")

        if (userDAO.isEmailRegistered(emailvalue))
            new JSONObject().put("error", emailTakenMessage(emailvalue))
        else
            new JSONObject()
    }

    private def emailTakenMessage(email : String) =
        "The email '"+email+"' is taken"

    def onValidateFromForm() {
        if (password != null && confirmemail != null && !password.equals(confirmpassword))
            form.recordError(confirmPasswordField, "Passwords do not match")

        if (email != null && confirmemail != null && !email.equals(confirmemail))
            form.recordError(confirmEmailField, "E-mails do not match")

        if (email != null && userDAO.isEmailRegistered(email))
            form.recordError(emailField, emailTakenMessage(email))
    }

    def onSuccessFromForm() = {
        createUserFlow.setEmail(email)
        createUserFlow.setPassword(password)
        createUserFlow.execute()

        classOf[LoginUser]
    }
}
