
package axirassa.webapp.services

import axirassa.config.Messaging
import axirassa.messaging.EmailRequestMessage
import org.hornetq.api.core.client.ClientProducer
import axirassa.services.email.EmailTemplate

trait EmailNotifyService {
  def startMessage(template: EmailTemplate)
  def addAttribute(key: String, value: Object)
  def send()
  def setToAddress(email: String)
}

class EmailNotifyServiceImpl(
  messagingSession: MessagingSession,
  var producer: ClientProducer,
  var request: EmailRequestMessage = null) extends EmailNotifyService {
  def this(messagingSession: MessagingSession) {
    this(messagingSession, messagingSession.createProducer(Messaging.NOTIFY_EMAIL_REQUEST))
  }

  override def startMessage(template: EmailTemplate) {
    request = new EmailRequestMessage(template)
  }

  override def addAttribute(key: String, value: Object) {
    request.addAttribute(key, value)
  }

  override def send() {
    val message = messagingSession.createMessage(true)
    message.getBodyBuffer().writeBytes(request.toBytes())
    producer.send(message)
    request = null
  }

  override def setToAddress(email: String) {
    request.toAddress = email
  }
}
