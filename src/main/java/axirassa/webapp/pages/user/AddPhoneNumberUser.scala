
package axirassa.webapp.pages.user

import java.io.IOException

import org.apache.shiro.authz.annotation.RequiresUser
import org.apache.tapestry5.Link
import org.apache.tapestry5.annotations.Component
import org.apache.tapestry5.annotations.Property
import org.apache.tapestry5.annotations.Secure
import org.apache.tapestry5.beaneditor.Validate
import org.apache.tapestry5.hibernate.annotations.CommitAfter
import org.apache.tapestry5.ioc.annotations.Inject
import org.apache.tapestry5.services.PageRenderLinkSource
import org.hibernate.Session
import org.hornetq.api.core.HornetQException

import axirassa.dao.UserDAO
import axirassa.model.UserEntity
import axirassa.model.UserPhoneNumberEntity
import axirassa.webapp.components.AxCheckbox
import axirassa.webapp.components.AxForm
import axirassa.webapp.services.AxirassaSecurityService
import axirassa.webapp.services.AxirassaSecurityException

@Secure
@RequiresUser
class AddPhoneNumberUser {
    @Inject
    var security : AxirassaSecurityService = _

    @Inject
    var database : Session = _

    @Inject
    var userDAO : UserDAO = _

    @Inject
    var linkSource : PageRenderLinkSource = _

    @Property
    @Validate("required,regexp=[-+*#0-9a-zA-Z.\\, ()]+,minlength=10")
    var phoneNumber : String = _

    @Property
    @Validate("regexp=[-*#0-9]+")
    var extension : String = _

    @Property
    var acceptsVoice : Boolean = _

    @Property
    var acceptsText : Boolean = _

    @Component
    var acceptsTextField : AxCheckbox = _

    @Component
    var acceptsVoiceField : AxCheckbox = _

    @Component
    var form : AxForm = _

    var token : String = _

    def onValidateFromForm {
        if (extension != null && acceptsText == true)
            form.recordError(acceptsTextField, "Text messages may not be sent to phone numbers with extensions")

        if (!acceptsText && !acceptsVoice)
            form.recordError("Please specify your notification method preference")
    }

    @CommitAfter
    def onSuccess() {
        val user = security.getUserEntity

        val phoneNumberEntity = new UserPhoneNumberEntity()
        phoneNumberEntity.setUser(user)
        phoneNumberEntity.setPhoneNumber(phoneNumber)
        phoneNumberEntity.setExtension(extension)
        phoneNumberEntity.setAcceptingSms(acceptsText)
        phoneNumberEntity.setAcceptingVoice(acceptsVoice)
        phoneNumberEntity.setConfirmed(false)
        database.save(phoneNumberEntity)

        linkSource.createPageRenderLinkWithContext(classOf[VerifyPhoneNumberUser], phoneNumberEntity.getId().asInstanceOf[Object])
    }
}
