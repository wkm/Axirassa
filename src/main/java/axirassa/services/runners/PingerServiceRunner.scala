
package axirassa.services.runners

import org.hornetq.api.core.client.ClientSession

import axirassa.services.Service
import axirassa.services.pinger.PingerService
import axirassa.util.MessagingTools

object PingerServiceRunner {
    def main(args : Array[String]) {
        val session = MessagingTools.getEmbeddedSession()
        val service = new PingerService(session)

        service.execute()
    }
}
