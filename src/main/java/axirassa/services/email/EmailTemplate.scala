package axirassa.services.email

import axirassa.services.util.Template
import axirassa.services.util.TemplateType
import axirassa.services.util.TemplateFactory

import scala.reflect.BeanProperty

object EmailTemplateType {
  val HTML = new EmailTemplateType("html")
  val TEXT = new EmailTemplateType("text")
  val SUBJECT = new EmailTemplateType("subject")
}
class EmailTemplateType(val extension: String) extends TemplateType

object EmailTemplate {
  val USER_VERIFY_ACCOUNT = new EmailTemplate("account/VerifyAccountEmail", EmailSenderAddress.ACCOUNT)
  val USER_RESET_PASSWORD = new EmailTemplate("account/ResetPasswordEmail", EmailSenderAddress.ACCOUNT)
  val USER_CHANGE_PASSWORD = new EmailTemplate("account/PasswordChangeEmail", EmailSenderAddress.ACCOUNT)
  val USER_PRIMARY_EMAIL_CHANGED = new EmailTemplate("account/PrimaryEmailChangedEmail", EmailSenderAddress.ACCOUNT)
  val AGGREGATED_FEEDBACK = new EmailTemplate("support/AggregatedFeedbackEmail", EmailSenderAddress.INTERNAL)

  val BASE_LOCATION = "/axirassa/webapp/emails/"

}
class EmailTemplate(var location: String, var fromAddress: String) extends Template {
  override def fullLocation = EmailTemplate.BASE_LOCATION + location
}

class EmailTemplateFactory
  extends TemplateFactory[EmailTemplate, EmailTemplateType](EmailTemplate.BASE_LOCATION)

object EmailTemplateFactory {
  val instance = new EmailTemplateFactory

  def getTemplateInstance(template: EmailTemplate, templateType: EmailTemplateType) =
    instance.getTemplate(template, templateType)
}