
package axirassa.messaging;

import lombok.Getter;
import axirassa.services.phone.PhoneTemplate;
import axirassa.services.util.TemplateFillingMessage;

class SmsRequestMessage extends TemplateFillingMessage {
	var phoneNumber : String = _
	var template : PhoneTemplate = _
}