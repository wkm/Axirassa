
package axirassa.services.runners

import org.quartz.Job
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException

import axirassa.ioc.IocStandalone
import axirassa.services.DatabaseCleanerService
class DatabaseCleanerServiceJob extends Job {
    override def execute(context : JobExecutionContext) {
        try {
            val service = IocStandalone.autobuild(classOf[DatabaseCleanerService])
            service.execute
        } catch {
            case e => throw new JobExecutionException(e)
        }
    }
}
