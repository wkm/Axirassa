
package axirassa.services.runners

import org.hibernate.Session
import org.hornetq.api.core.client.ClientSession
import org.quartz.Job
import org.quartz.JobDataMap
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException

import axirassa.services.InjectorService
import axirassa.services.Service

object InjectorServiceJob {
    val DATABASE_SESSION = "session.database"
    val MESSAGING_SESSION = "session.messaging"
}

class InjectorServiceJob extends Job {
    override def execute(context : JobExecutionContext) {
        try {
            val datamap = context.getJobDetail().getJobDataMap()

            val messagingSession = datamap.get(InjectorServiceJob.MESSAGING_SESSION).asInstanceOf[ClientSession]
            val databaseSession = datamap.get(InjectorServiceJob.DATABASE_SESSION).asInstanceOf[Session]

            val service = new InjectorService(messagingSession, databaseSession)
            service.execute
        } catch {
            case e => throw new JobExecutionException(e)
        }
    }
}
