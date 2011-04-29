
package axirassa.services.phone

import org.apache.http.client.HttpClient
import org.apache.http.impl.client.DefaultHttpClient
import org.hornetq.api.core.client.ClientConsumer
import org.hornetq.api.core.client.ClientMessage
import org.hornetq.api.core.client.ClientSession

import axirassa.config.Messaging
import axirassa.messaging.VoiceRequestMessage
import axirassa.services.Service
import axirassa.util.AutoSerializingObject

class VoiceNotificationService(messaging : ClientSession) extends Service {
    val httpClient = new DefaultHttpClient()

    override def execute {
        val consumer = messaging.createConsumer(Messaging.NOTIFY_VOICE_REQUEST)
        while (true) {
            try {
                val message = consumer.receive()
                println("####\n####\n####\n####\n####\n####\n")
                println("RECIEVED MESSAGE: "+message)

                val buffer = new Array[Byte](message.getBodyBuffer().readableBytes())
                message.getBodyBuffer().readBytes(buffer)

                val rawobject = AutoSerializingObject.fromBytes(buffer)
                rawobject match {
                    case msg : VoiceRequestMessage => {
                        val text = PhoneTemplateFactory.instance.getText(
                            msg.getTemplate,
                            PhoneTemplateType.VOICE,
                            msg.getAttributeMap)

                        val sender = new SendVoice(msg.getPhoneNumber(), msg.getExtension(), text)
                        sender.send(httpClient)
                    }
                }

                message.acknowledge()
                messaging.commit()
            } catch {
                case e : ClassNotFoundException => e.printStackTrace()
            }
        }
    }
}
