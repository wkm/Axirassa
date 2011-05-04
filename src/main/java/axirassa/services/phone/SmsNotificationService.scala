
package axirassa.services.phone

import org.apache.http.impl.client.DefaultHttpClient
import org.hornetq.api.core.client.ClientSession

import axirassa.config.Messaging
import axirassa.messaging.SmsRequestMessage
import axirassa.services.Service
import axirassa.util.AutoSerializingObject

class SmsNotificationService(messagingSession : ClientSession) extends Service {
    val httpClient = new DefaultHttpClient

    override def execute {
        messagingSession.start()

        val consumer = messagingSession.createConsumer(Messaging.NOTIFY_SMS_REQUEST)

        while (true) {
            try {
                val message = consumer.receive()
                val buffer = new Array[Byte](message.getBodyBuffer().readableBytes())
                message.getBodyBuffer().readBytes(buffer)

                val rawobject = AutoSerializingObject.fromBytes(buffer)

                rawobject match {
                    case msg : SmsRequestMessage => {
                        val text = PhoneTemplateFactory.instance.getText(msg.template, PhoneTemplateType.SMS, msg.attributeMap)

                        val sender = new SendSMS(msg.phoneNumber, text)
                        sender.send(httpClient)
                    }
                }

                message.acknowledge()
                messagingSession.commit()
            } catch {
                case e : ClassNotFoundException => e.printStackTrace()
            }
        }
    }
}
