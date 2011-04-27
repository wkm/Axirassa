package axirassa.webapp.pages.user;

import axirassa.dao.UserPhoneNumberDAO;
import axirassa.model.UserPhoneNumberEntity;
import axirassa.webapp.components.AxSubmit;
import axirassa.webapp.services.AxirassaSecurityService;
import axirassa.webapp.services.AxirassaSecurityException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;

@RequiresAuthentication
class DeletePhoneNumberUser {
    @Inject
    var session : Session = _

    @Inject
    var userPhoneNumberDAO : UserPhoneNumberDAO = _

    @Inject
    var security : AxirassaSecurityService = _

    @Property
    var phoneNumber : UserPhoneNumberEntity = _

    @Component
    var cancelChanges : AxSubmit = _

    @Component
    var delete : AxSubmit = _

    def onActivate() {
        classOf[SettingsUser]
    }

    def onActivate(phoneNumberId : Long) {
        val phoneRecord = userPhoneNumberDAO.getByIdWithUser(phoneNumberId)
        if (phoneRecord.isEmpty)
            classOf[SettingsUser]
        else {
            phoneNumber = phoneRecord.get
            security.verifyOwnership(phoneNumber)
            true
        }

    }

    def onPassivate() {
        phoneNumber.getId()
    }

    def onSelectedFromCancelChanges() {
        classOf[SettingsUser]
    }

    @CommitAfter
    def onSelectedFromDelete() {
        session.delete(phoneNumber)
        classOf[SettingsUser]
    }
}
