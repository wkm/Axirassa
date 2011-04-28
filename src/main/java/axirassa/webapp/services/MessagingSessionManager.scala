
package axirassa.webapp.services
import org.hornetq.api.core.HornetQException

import org.apache.tapestry5.ioc.services.ThreadCleanupListener

trait MessagingSessionManager extends ThreadCleanupListener {
	def getSession : MessagingSession
	override def threadDidCleanup
}

class MessagingSessionManagerImpl extends MessagingSessionManager {
	var session : MessagingSession = new MessagingSessionImpl(MessagingTools.getEmbeddedSession())
	
	override
	def getSession = session

	override
	def threadDidCleanup {
		try {
			System.err.println("CLEANING UP AFTER THREAD")
			session.close
		} catch {
		    case e : HornetQException => e.printStackTrace()
		}
	}
}
