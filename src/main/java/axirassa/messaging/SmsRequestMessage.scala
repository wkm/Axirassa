
package axirassa.messaging;

import lombok.Getter;
import axirassa.services.phone.PhoneTemplate;
import axirassa.services.util.TemplateFillingMessage;

class SmsRequestMessage(phoneNumber : String, template:PhoneTemplate) extends TemplateFillingMessage;