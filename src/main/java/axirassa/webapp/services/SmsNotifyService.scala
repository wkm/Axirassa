
package axirassa.webapp.services

import axirassa.messaging.SmsRequestMessage
import axirassa.config.Messaging
import java.util.HashMap
import org.hornetq.api.core.client.ClientProducer
import java.io.IOException
import org.hornetq.api.core.HornetQException
import axirassa.services.phone.PhoneTemplate

trait SmsNotifyService {
	def startMessage(template : PhoneTemplate)
	def addAttribute(key : String, value : String )
	def send
	def setPhoneNumber(phoneNumber : String)
}

class SmsNotifyServiceImpl(messagingSession : MessagingSession) extends SmsNotifyService {
	val producer = messagingSession.createProducer(Messaging.NOTIFY_SMS_REQUEST)

	var template : PhoneTemplate = _
	var attributes : HashMap[String, Object] = _
	var phoneNumber : String

	def startMessage(template : PhoneTemplate) {
		reset()
		this.template = template
	}


	private def reset() {
		attributes.clear()
	}


	def addAttribute(key : String , value : String ) {
		attributes.put(key, value)
	}


	override
	def setPhoneNumber(phoneNumber : String) {
		this.phoneNumber = phoneNumber
	}


	override
	def send {
		val message = messagingSession.createMessage(true)

		val request = new SmsRequestMessage(phoneNumber, template)
		request.addAttributes(attributes)

		message.getBodyBuffer().writeBytes(request.toBytes())
		producer.send(message)

		reset()
	}
}
