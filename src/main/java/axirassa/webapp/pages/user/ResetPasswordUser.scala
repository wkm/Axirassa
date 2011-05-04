package axirassa.webapp.pages.user

import axirassa.dao.UserDAO
import axirassa.model.PasswordResetTokenEntity
import axirassa.services.email.EmailTemplate
import axirassa.webapp.components.AxForm
import axirassa.webapp.services.EmailNotifyService
import org.apache.shiro.authz.annotation.RequiresGuest
import org.apache.tapestry5.annotations.Component
import org.apache.tapestry5.annotations.Property
import org.apache.tapestry5.annotations.Secure
import org.apache.tapestry5.hibernate.annotations.CommitAfter
import org.apache.tapestry5.ioc.annotations.Inject
import org.apache.tapestry5.services.PageRenderLinkSource
import org.hibernate.Session
@Secure
@RequiresGuest
class ResetPasswordUser {

    @Inject
    var database : Session = _

    @Inject
    var userDAO : UserDAO = _

    @Inject
    var linkSource : PageRenderLinkSource = _

    @Inject
    var emailNotify : EmailNotifyService = _

    @Property
    var email : String = _

    @Component
    var form : AxForm = _

    def onValidateFromForm() {
        if (email == null) {
            showInvalidEmailMessage()
            return
        }

        val entity = userDAO.getUserByEmail(email)
        if (entity.isEmpty)
            showInvalidEmailMessage()
    }

    private def showInvalidEmailMessage() {
        form.recordError("No user associated with that e-mail.")
    }

    @CommitAfter
    def onSuccessFromForm() = {
        val user = userDAO.getUserByEmail(email)
        val token = new PasswordResetTokenEntity()
        token.setUser(user.get)
        database.save(token)

        val link = linkSource.createPageRenderLinkWithContext(classOf[ChangePasswordByTokenUser], token.getToken())
            .toAbsoluteURI(true)

        emailNotify.startMessage(EmailTemplate.USER_RESET_PASSWORD)
        emailNotify.setToAddress(email)
        emailNotify.addAttribute("axlink", link)
        emailNotify.send()

        classOf[PasswordResetSentUser]
    }
}
