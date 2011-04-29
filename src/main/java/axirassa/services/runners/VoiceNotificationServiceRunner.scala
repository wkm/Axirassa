
package axirassa.services.runners

import org.hornetq.api.core.client.ClientSession

import axirassa.services.Service
import axirassa.services.phone.VoiceNotificationService
import axirassa.util.MessagingTools

object VoiceNotificationServiceRunner {
	def main(args : Array[String]) {
		val session = MessagingTools.getEmbeddedSession()
		val service = new VoiceNotificationService(session)

		service.execute
	}
}
