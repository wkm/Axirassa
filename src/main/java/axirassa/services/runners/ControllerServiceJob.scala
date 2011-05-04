
package axirassa.services.runners

import org.quartz.Job
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException

import axirassa.ioc.IocStandalone
import axirassa.services.ControllerService
class ControllerServiceJob extends Job {
	override
	def execute (context : JobExecutionContext) {
		try {
			val service = IocStandalone.autobuild(classOf[ControllerService])
			service.execute
		} catch {
			case ex => throw new JobExecutionException(ex)
		}
	}
}
