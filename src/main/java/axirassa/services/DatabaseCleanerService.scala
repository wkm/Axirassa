
package axirassa.services

import org.apache.tapestry5.ioc.annotations.Inject
import org.hibernate.Session

import axirassa.dao.PasswordResetTokenDAO
import axirassa.ioc.IocStandalone
import axirassa.util.MessagingTools

object DatabaseCleanerService {
	def main (args : Array[String]) {
		val service = IocStandalone.autobuild(classOf[DatabaseCleanerService])
		service.execute
	}
}

class DatabaseCleanerService extends Service {

	@Inject
	var database : Session = _
	
	@Inject
	var passwordTokens : PasswordResetTokenDAO = _


	@Override
	def execute {
		removeExpiredTokens
		aggregateAndSendFeedback
	}


	private def aggregateAndSendFeedback {
        val service = IocStandalone.autobuild(classOf[FeedbackAggregationService])
		service.execute()
	}


	private def removeExpiredTokens {
		database.beginTransaction()

		val removed = passwordTokens.removeExpiredTokens
		System.out.println("Removed " + removed + " expired password tokens")

		database.getTransaction().commit()
	}
}
