
package axirassa.services.runners

import org.quartz.CronTrigger
import org.quartz.JobDetail
import org.quartz.impl.StdSchedulerFactory

object ControllerServiceRunner {
    val JOB_NAME = "EveryMinute"
    val JOB_GROUP = "AxController"

    def main(args : Array[String]) {
        val scheduler = StdSchedulerFactory.getDefaultScheduler()
        scheduler.start()

        val job = new JobDetail(JOB_NAME, JOB_GROUP, classOf[ControllerServiceJob])
        val trigger = new CronTrigger(JOB_NAME, JOB_GROUP, "0/60 * * * * ?")

        scheduler.scheduleJob(job, trigger)
    }
}
