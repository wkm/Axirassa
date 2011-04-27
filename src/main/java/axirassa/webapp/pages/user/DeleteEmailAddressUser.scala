package axirassa.webapp.pages.user

import axirassa.dao.UserEmailAddressDAO
import axirassa.model.UserEmailAddressEntity
import axirassa.webapp.components.AxSubmit
import axirassa.webapp.services.AxirassaSecurityService
import axirassa.webapp.services.AxirassaSecurityException
import org.apache.shiro.authz.annotation.RequiresAuthentication
import org.apache.tapestry5.annotations.Component
import org.apache.tapestry5.annotations.Property
import org.apache.tapestry5.hibernate.annotations.CommitAfter
import org.apache.tapestry5.ioc.annotations.Inject
import org.hibernate.Session

@RequiresAuthentication
class DeleteEmailAddressUser {
    @Inject
    var database : Session = _

    @Inject
    var security : AxirassaSecurityService = _

    @Inject
    var userEmailAddressDAO : UserEmailAddressDAO = _

    @Property
    var email : UserEmailAddressEntity = _

    @Component
    var cancelChanges : AxSubmit = _

    @Component
    var delete : AxSubmit = _

    def onActivate() {
        classOf[SettingsUser]
    }

    def onActivate(emailId : Long) {
        val emailEntity = userEmailAddressDAO.getByIdWithUser(emailId)

        if (emailEntity == null)
            classOf[SettingsUser]
        else {
            email = emailEntity.get
            security.verifyOwnership(email)
            true
        }
    }

    def onPassivate() = {
        email.getId()
    }

    def onSelectedFromCancelChanges() = {
        classOf[SettingsUser]
    }

    @CommitAfter
    def onSelectedFromDelete() = {
        database.delete(email)
        classOf[SettingsUser]
    }
}
