
package axirassa.messaging;



import axirassa.services.email.EmailTemplate;
import axirassa.services.util.TemplateFillingMessage;

class EmailRequestMessage(
  var template : EmailTemplate,
  var toAddress : String = null) extends TemplateFillingMessage
