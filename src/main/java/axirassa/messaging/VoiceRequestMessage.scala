
package axirassa.messaging

import axirassa.services.phone.PhoneTemplate
import axirassa.services.util.TemplateFillingMessage

class VoiceRequestMessage(
  var phoneNumber : String,
  var extension : String,
  var template : PhoneTemplate) extends TemplateFillingMessage