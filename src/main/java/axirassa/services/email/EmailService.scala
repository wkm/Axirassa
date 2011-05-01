
package axirassa.services.email

import java.io.IOException

import org.apache.http.impl.client.DefaultHttpClient
import org.hornetq.api.core.HornetQException
import org.hornetq.api.core.client.ClientConsumer
import org.hornetq.api.core.client.ClientMessage
import org.hornetq.api.core.client.ClientSession
import org.hornetq.utils.json.JSONException

import axirassa.config.Messaging
import axirassa.messaging.EmailRequestMessage
import axirassa.services.Service
import axirassa.util.AutoSerializingObject
import freemarker.template.TemplateException

class EmailService(messagingSession : ClientSession) extends Service {
  val httpClient = new DefaultHttpClient()

  override def execute() {
    messagingSession.start()

    val consumer = messagingSession.createConsumer(Messaging.NOTIFY_EMAIL_REQUEST)

    while (true) {
      try {
        val message = consumer.receive()

        System.out.println("Received message: "+message)

        val buffer = new Array[Byte](message.getBodyBuffer.readableBytes())
        message.getBodyBuffer().readBytes(buffer)

        val rawobject = AutoSerializingObject.fromBytes(buffer)
        rawobject match {
          case emailRequest : EmailRequestMessage => {
            val composer = new EmailTemplateComposer(emailRequest.template)
            composer.setAttributes(emailRequest.attributeMap)

            val sender = new EmailSender(composer, emailRequest.toAddress)
            sender.send(httpClient)
          }
        }

        message.acknowledge()
        messagingSession.commit()
      } catch {
        case e : ClassNotFoundException =>
        case e : JSONException          =>
        case e : TemplateException      =>
      }
    }
  }
}
