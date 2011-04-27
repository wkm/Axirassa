
package axirassa.webapp.services

import java.io.IOException

import org.hornetq.api.core.HornetQException

import axirassa.services.email.EmailTemplate

trait EmailNotifyService {
	def startMessage(template : EmailTemplate )
	def addAttribute(key : String , value : Object )
	def send()
	def setToAddress(email : String )
}

class EmailNotifyServiceImpl extends  EmailNotifyService {
	var messagingSession : MessagingSession = _ 
	var producer :  ClientProducer = _ 
	var  request : EmailRequestMessage = _


	def this (messagingSession : MessagingSession)  {
		this.messagingSession = messagingSession
		producer = messagingSession.createProducer(Messaging.NOTIFY_EMAIL_REQUEST)
	}


	override
	def startMessage(template : EmailTemplate ) {
		request = new EmailRequestMessage(template)
	}


	override
	def addAttribute(key : String , value: Object ) {
		request.addAttribute(key, value)
	}


	override
	def send() {
		val message = messagingSession.createMessage(true)
		message.getBodyBuffer().writeBytes(request.toBytes())
		producer.send(message)
		request = null
	}


	override
	def setToAddress(email : String ) {
		request.setToAddress(email)
	}
}
