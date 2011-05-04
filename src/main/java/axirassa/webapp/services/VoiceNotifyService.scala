
package axirassa.webapp.services

import axirassa.messaging.VoiceRequestMessage
import axirassa.config.Messaging
import java.util.HashMap
import axirassa.services.phone.PhoneTemplate

trait VoiceNotifyService {
	def startMessage(template : PhoneTemplate)
	def addAttribute(key : String, value : String)
	def send
	def setPhoneNumber(phoneNumber : String)
	def setExtension(extension : String)
}

class VoiceNotifyServiceImpl(messagingSession : MessagingSession) extends VoiceNotifyService {
	val producer = messagingSession.createProducer(Messaging.NOTIFY_VOICE_REQUEST)

	var template : PhoneTemplate = _
	var attributes = new HashMap[String,Object]()
	var phoneNumber : String = _
	var extension : String = _


	override
	def startMessage(template : PhoneTemplate) {
		reset()
		this.template = template
	}


	private def reset() {
		attributes.clear()
	}


	override
	def addAttribute(key : String, value : String) {
		attributes.put(key, value)
	}


	override
	def setPhoneNumber(phoneNumber : String ) {
		this.phoneNumber = phoneNumber
	}


	override
	def setExtension(extension : String) {
		this.extension = extension
	}


	override
	def send() {
		val message = messagingSession.createMessage(true)

		val request = new VoiceRequestMessage(phoneNumber, extension, template)
		request.addAttributes(attributes)

		message.getBodyBuffer().writeBytes(request.toBytes())
		producer.send(message)

		reset()
	}
}