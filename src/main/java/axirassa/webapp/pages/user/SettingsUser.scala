
package axirassa.webapp.pages.user

import java.io.IOException

import org.apache.shiro.authz.annotation.RequiresAuthentication
import org.apache.tapestry5.PersistenceConstants
import org.apache.tapestry5.annotations.Component
import org.apache.tapestry5.annotations.Persist
import org.apache.tapestry5.annotations.Property
import org.apache.tapestry5.annotations.Secure
import org.apache.tapestry5.hibernate.annotations.CommitAfter
import org.apache.tapestry5.ioc.annotations.Inject
import org.hornetq.api.core.HornetQException

import axirassa.dao.UserEmailAddressDAO
import axirassa.dao.UserPhoneNumberDAO
import axirassa.model.UserEmailAddressEntity
import axirassa.model.UserEntity
import axirassa.model.UserPhoneNumberEntity
import axirassa.model.NoSaltException
import axirassa.services.email.EmailTemplate
import axirassa.webapp.components.AxForm
import axirassa.webapp.components.AxPasswordField
import axirassa.webapp.services.AxirassaSecurityService
import axirassa.webapp.services.EmailNotifyService
import axirassa.webapp.services.AxirassaSecurityException

@Secure
@RequiresAuthentication
class SettingsUser {

    @Inject
    var security : AxirassaSecurityService = _

    @Inject
    var userPhoneNumberDAO : UserPhoneNumberDAO = _

    @Inject
    var userEmailAddressDAO : UserEmailAddressDAO = _

    @Inject
    var emailNotify : EmailNotifyService = _

    @Property
    @Persist
    var user : UserEntity = _

    def onActivate() {
        user = security.getUserEntity

        val phoneNumberRecords = 
        phoneNumbers = userPhoneNumberDAO.getPhoneNumbersByUser(user)
        if (phoneNumbers.size > 0)
            hasPhoneNumbers = true
        else
            hasPhoneNumbers = false

        emails = userEmailAddressDAO.getEmailsByUser(user)
        if (emails.size > 0)
            hasAlternateEmails = true
        else
            hasAlternateEmails = false

        return true
    }

    //
    // E-Mails
    //

    @Property
    var hasAlternateEmails : Boolean = _

    @Property
    var emails : List[UserEmailAddressEntity] = _

    @Property
    var email : UserEmailAddressEntity = _

    //
    // Phone Numbers
    //
    @Property
    var hasPhoneNumbers : Boolean = _

    @Property
    var phoneNumbers : List[UserPhoneNumberEntity] = _

    @Property
    var phoneNumber : UserPhoneNumberEntity = _

    //
    // Password
    //
    @Property
    var currentPassword : String = _

    @Property
    var newPassword : String = _

    @Property
    var confirmPassword : String = _

    @Component
    var currentPasswordField : AxPasswordField = _

    @Component
    var confirmPasswordField : AxPasswordField = _

    @Component
    var passwordForm : AxForm = _

    @Property
    @Persist(PersistenceConstants.FLASH)
    var passwordChanged : Boolean = _

    def onValidateFromPasswordForm() {
        if (currentPassword != null)
            validateCurrentPassword()

        if (newPassword != null && confirmPassword != null && !newPassword.equals(confirmPassword))
            passwordForm.recordError(confirmPasswordField, "Passwords do not match")
    }

    private def validateCurrentPassword() {
        if (!user.matchPassword(currentPassword))
            passwordForm.recordError(currentPasswordField, "Incorrect password")
    }

    @CommitAfter
    def onSuccessFromPasswordForm() {
        user.createPassword(newPassword)
        passwordChanged = true

        emailNotify.startMessage(EmailTemplate.USER_CHANGE_PASSWORD)
        emailNotify.setToAddress(userEmailAddressDAO.getPrimaryEmail(user).get.getEmail())
        emailNotify.send()

        return this
    }

}
