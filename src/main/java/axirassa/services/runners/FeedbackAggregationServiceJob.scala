
package axirassa.services.runners

import axirassa.ioc.IocStandalone
import org.quartz.Job
import org.quartz.JobDataMap
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException

import axirassa.services.FeedbackAggregationService
import axirassa.services.Service

object FeedbackAggregationServiceJob {
    val DATABASE_SESSION = "session.database"
    val MESSAGING_SESSION = "session.messaging"
}

class FeedbackAggregationServiceJob extends Job {

    override def execute(context : JobExecutionContext) {
        try {
            val datamap = context.getJobDetail().getJobDataMap()
            val service = IocStandalone.autobuild(classOf[FeedbackAggregationService])
            service.execute()
        } catch {
            case e => throw new JobExecutionException(e)
        }
    }
}
