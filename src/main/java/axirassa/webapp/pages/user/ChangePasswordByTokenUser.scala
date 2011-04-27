package axirassa.webapp.pages.user

import axirassa.dao.PasswordResetTokenDAO
import axirassa.model.PasswordResetTokenEntity
import axirassa.model.UserEntity
import axirassa.webapp.components.AxForm
import axirassa.webapp.components.AxPasswordField
import axirassa.webapp.pages.Index
import org.apache.tapestry5.annotations.Component
import org.apache.tapestry5.annotations.Persist
import org.apache.tapestry5.annotations.Property
import org.apache.tapestry5.hibernate.annotations.CommitAfter
import org.apache.tapestry5.ioc.annotations.Inject
import org.hibernate.Session

class ChangePasswordByTokenUser {

    @Inject
    var database : Session = _

    @Inject
    var passwordResetTokenDAO : PasswordResetTokenDAO = _

    @Property
    @Persist
    var token : String = _

    @Persist
    var tokenEntity : PasswordResetTokenEntity = _

    @Property
    @Persist
    var user : UserEntity = _

    @Property
    var isTokenInvalid = false

    @Property
    var newPassword : String = _

    @Property
    var confirmPassword : String = _

    @Component
    var confirmPasswordField : AxPasswordField = _

    @Component
    var form : AxForm = _

    def onActivate() = {
        classOf[Index]
    }

    def onActivate(token : String) : Boolean = {
        this.token = token
        val entity = passwordResetTokenDAO.getByToken(token)

        if (entity.isEmpty) {
            isTokenInvalid = true
            return true
        }

        tokenEntity = entity.get
        isTokenInvalid = false
        user = tokenEntity.getUser()

        return true
    }

    def onPassivate() = {
        println("passivating")
        token
    }

    def onValidateFromForm() {
        println("validating form")
        if (newPassword != null && confirmPassword != null && !newPassword.equals(confirmPassword))
            form.recordError(confirmPasswordField, "Passwords do not match")
    }

    @CommitAfter
    def onSuccessFromForm() = {
        System.out.println("form success")

        if (user == null)
            true
        else {

            if (!isTokenInvalid) {
                System.out.println("saving")
                user.createPassword(newPassword)
                database.update(user)
                database.delete(tokenEntity)
            }

            classOf[LoginUser]
        }
    }
}
