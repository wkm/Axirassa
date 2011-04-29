
package axirassa.services.runners

import org.quartz.CronTrigger
import org.quartz.JobDataMap
import org.quartz.JobDetail
import org.quartz.Scheduler
import org.quartz.Trigger
import org.quartz.impl.StdSchedulerFactory

import axirassa.util.HibernateTools
import axirassa.util.MessagingTools

object DatabaseCleanerServiceRunner {
    val JOB_GROUP = "AxDatabaseCleaner"
    val DB_CLEAN = "DB_CLEAN"
    val FEEDBACK_SEND = "FEEDBACK_SEND"

    def main(args : Array[String]) {
        val scheduler = StdSchedulerFactory.getDefaultScheduler()
        scheduler.start()

        val dbCleanJob = new JobDetail(DB_CLEAN, JOB_GROUP, classOf[DatabaseCleanerServiceJob])
        val dbCleanTrigger = new CronTrigger(DB_CLEAN, JOB_GROUP, "0 0 * * * ?")
        scheduler.scheduleJob(dbCleanJob, dbCleanTrigger)

        val datamap = new JobDataMap()
        datamap.put(FeedbackAggregationServiceJob.DATABASE_SESSION, HibernateTools.getLightweightSession())
        datamap.put(FeedbackAggregationServiceJob.MESSAGING_SESSION, MessagingTools.getEmbeddedSession())

        val feedbackJob = new JobDetail(FEEDBACK_SEND, JOB_GROUP, classOf[FeedbackAggregationServiceJob])
        feedbackJob.setJobDataMap(datamap)

        val feedbackTrigger = new CronTrigger(FEEDBACK_SEND, JOB_GROUP, "0 0/1 * * * ?")
        scheduler.scheduleJob(feedbackJob, feedbackTrigger)
    }
}
