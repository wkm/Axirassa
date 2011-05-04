
package axirassa.services.runners

import axirassa.services.phone.SmsNotificationService
import axirassa.util.MessagingTools

object SmsNotificationServiceRunner {
  def main(args : Array[String]) {
    val session = MessagingTools.getEmbeddedSession()
    val service = new SmsNotificationService(session)

    service.execute
  }
}
