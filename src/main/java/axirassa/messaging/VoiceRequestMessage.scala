
package axirassa.messaging;

import axirassa.services.phone.PhoneTemplate;
import axirassa.services.util.TemplateFillingMessage;

class VoiceRequestMessage(phoneNumber : String, extension:String, template :PhoneTemplate) extends TemplateFillingMessage