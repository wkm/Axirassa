package axirassa.webapp.pages.user

import axirassa.dao.UserPhoneNumberDAO
import axirassa.model.UserPhoneNumberEntity
import axirassa.services.phone.PhoneTemplate
import axirassa.webapp.components.AxForm
import axirassa.webapp.components.AxTextField
import axirassa.webapp.services.AxirassaSecurityService
import axirassa.webapp.services.SmsNotifyService
import axirassa.webapp.services.VoiceNotifyService
import axirassa.webapp.services.exceptions.AxirassaSecurityException
import org.apache.shiro.authz.annotation.RequiresAuthentication
import org.apache.tapestry5.annotations.Component
import org.apache.tapestry5.annotations.Persist
import org.apache.tapestry5.annotations.Property
import org.apache.tapestry5.hibernate.annotations.CommitAfter
import org.apache.tapestry5.ioc.annotations.Inject
import org.hibernate.Session
import org.hornetq.api.core.HornetQException

import java.io.IOException

@RequiresAuthentication
class VerifyPhoneNumberUser {
    @Inject
    var database : Session = _

    @Inject
    var userPhoneNumberDAO : UserPhoneNumberDAO = _

    @Inject
    var security : AxirassaSecurityService = _

    @Inject
    var voice : VoiceNotifyService = _

    @Inject
    var sms : SmsNotifyService = _

    @Property
    @Persist
    var token : String = _

    @Property
    var phoneNumberEntity : UserPhoneNumberEntity = _

    @Component
    var form : AxForm = _

    @Component
    var verificationCodeField : AxTextField = _

    @Property
    var verificationCode : String = _

    var phoneNumberId : Long = _

    def onActivate(phoneNumberId : Long) = {
        this.phoneNumberId = phoneNumberId
        val phoneNumberRecord = userPhoneNumberDAO.getByIdWithUser(phoneNumberId)
        if(phoneNumberRecord.isEmpty)
            throw new AxirassaSecurityException
          
        phoneNumberEntity = phoneNumberRecord.get
        security.verifyOwnership(phoneNumberEntity)

        if (phoneNumberEntity.isConfirmed())
            classOf[SettingsUser]
        else
            true
    }

    def onPassivate() = {
        phoneNumberId
    }

    def onActionFromSendSMS(phoneNumberId : Long) = {
        phoneNumberEntity = userPhoneNumberDAO.getByIdWithUser(phoneNumberId).get
        security.verifyOwnership(phoneNumberEntity)

        if (!phoneNumberEntity.isAcceptingSms())
            false
        else {
            sendCodeBySms(phoneNumberEntity.getFormattedToken())
            true
        }
    }

    def onActionFromSendVoice(phoneNumberId : Long) = {
        phoneNumberEntity = userPhoneNumberDAO.getByIdWithUser(phoneNumberId).get
        security.verifyOwnership(phoneNumberEntity)

        if (!phoneNumberEntity.isAcceptingVoice())
            false
        else {
            sendCodeByVoice(phoneNumberEntity.getFormattedToken())
            true
        }
    }

    def onValidateFromForm() {
        if (!verificationCode.equals(phoneNumberEntity.getToken()))
            form.recordError(verificationCodeField, "Verification code not matched")
    }

    @CommitAfter
    def onSuccessFromForm() = {
        phoneNumberEntity.setConfirmed(true)
        classOf[SettingsUser]
    }

    private def sendCodeByVoice(token : String) {
        voice.startMessage(PhoneTemplate.USER_VERIFY_PHONE_NUMBER)
        voice.setPhoneNumber(phoneNumberEntity.getPhoneNumber())
        voice.setExtension(phoneNumberEntity.getExtension())
        voice.addAttribute("code", token)
        voice.addAttribute("user", security.getEmail)
        voice.send()
    }

    private def sendCodeBySms(token : String) {
        sms.startMessage(PhoneTemplate.USER_VERIFY_PHONE_NUMBER)
        sms.setPhoneNumber(phoneNumberEntity.getPhoneNumber())
        sms.addAttribute("code", token)
        sms.addAttribute("user", security.getEmail)
        sms.send()
    }
}
