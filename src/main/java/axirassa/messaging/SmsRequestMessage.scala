
package axirassa.messaging

import axirassa.services.phone.PhoneTemplate
import axirassa.services.util.TemplateFillingMessage

class SmsRequestMessage(var phoneNumber : String, var template : PhoneTemplate) extends TemplateFillingMessage