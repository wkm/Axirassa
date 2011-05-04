
package axirassa.services.email

import java.util.HashMap
import java.util.Map

class EmailTemplateComposer(template : EmailTemplate) {
  val attributes = new HashMap[String, Object]

  def setAttributes(attributes : Map[String, Object]) {
    this.attributes.putAll(attributes)
  }

  def addAttribute(key : String, value : Object) {
    attributes.put(key, value)
  }

  def composeSubject() = EmailTemplateFactory.instance.getText(template, EmailTemplateType.SUBJECT, attributes)

  def composeHtml() = EmailTemplateFactory.instance.getText(template, EmailTemplateType.HTML, attributes)

  def composeText() = EmailTemplateFactory.instance.getText(template, EmailTemplateType.TEXT, attributes)

  def getEmailTemplate() = template
}
