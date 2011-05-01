package axirassa.services.phone

import axirassa.services.util.TemplateFactory
import axirassa.services.util.TemplateReference
import axirassa.services.util.TemplateType

object PhoneTemplateType {
  val VOICE = new PhoneTemplateType("voice")
  val SMS = new PhoneTemplateType("sms")
}
class PhoneTemplateType(val extension: String) extends TemplateType

object PhoneTemplate {
  val BASE_LOCATION = "/axirassa/webapp/messages/"

  val USER_VERIFY_PHONE_NUMBER = new PhoneTemplate("account/VerifyPhoneNumber")
}

class PhoneTemplate(val location: String) extends TemplateReference {
  override def fullLocation =
    PhoneTemplate.BASE_LOCATION + location
}

class PhoneTemplateFactory
  extends TemplateFactory[PhoneTemplate, PhoneTemplateType](PhoneTemplate.BASE_LOCATION);

object PhoneTemplateFactory {
  val instance = new PhoneTemplateFactory

  def getTemplateInstance(template: PhoneTemplate, templateType: PhoneTemplateType) =
    instance.getTemplate(template, templateType)
}