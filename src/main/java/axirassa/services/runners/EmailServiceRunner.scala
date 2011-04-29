
package axirassa.services.runners

import org.hornetq.api.core.client.ClientSession

import axirassa.services.Service
import axirassa.services.email.EmailService
import axirassa.util.MessagingTools

object EmailServiceRunner {
    def main(context : Array[String]) {
        val session = MessagingTools.getEmbeddedSession()
        val service = new EmailService(session)
        service.execute()
    }
}
