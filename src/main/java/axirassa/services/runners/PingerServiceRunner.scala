
package axirassa.services.runners

import axirassa.services.pinger.PingerService
import axirassa.util.MessagingTools

object PingerServiceRunner {
    def main(args : Array[String]) {
        val session = MessagingTools.getEmbeddedSession()
        val service = new PingerService(session)

        service.execute()
    }
}
