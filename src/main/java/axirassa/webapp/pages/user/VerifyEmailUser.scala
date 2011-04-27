package axirassa.webapp.pages.user

import axirassa.dao.UserEmailAddressDAO
import axirassa.model.UserEmailAddressEntity
import org.apache.tapestry5.annotations.Property
import org.apache.tapestry5.hibernate.annotations.CommitAfter
import org.apache.tapestry5.ioc.annotations.Inject
import org.hibernate.Session

class VerifyEmailUser {

    @Inject
    var database : Session = _

    @Inject
    var userEmailAddressDAO : UserEmailAddressDAO = _

    @Property
    var isTokenInvalid : Boolean = _

    def onActivate() = {
        classOf[SettingsUser]
    }

    @CommitAfter
    def onActivate(token : String) {
        val emailAddressRecord = userEmailAddressDAO.getByToken(token)

        if (emailAddressRecord.isEmpty) {
            isTokenInvalid = true
            true
        } else {
            val emailAddress = emailAddressRecord.get
            emailAddress.setVerified(true)
            database.save(emailAddress)

            classOf[SettingsUser]
        }
    }
}
