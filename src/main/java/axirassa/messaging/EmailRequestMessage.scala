
package axirassa.messaging;

import scala.reflect.BeanProperty
import java.io.Serializable;

import axirassa.services.email.EmailTemplate;
import axirassa.services.util.TemplateFillingMessage;

class EmailRequestMessage(
  var template : EmailTemplate,
  var toAddress : String = null) extends TemplateFillingMessage
