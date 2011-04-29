
package axirassa.services.runners

import org.hibernate.Session
import org.hornetq.api.core.client.ClientSession
import org.quartz.CronTrigger
import org.quartz.JobDataMap
import org.quartz.JobDetail
import org.quartz.Scheduler
import org.quartz.Trigger
import org.quartz.impl.StdSchedulerFactory

import axirassa.util.HibernateTools
import axirassa.util.MessagingTools

object InjectorServiceRunner {
	val JOB_GROUP = "AxInjector"
	val JOB = "EVERY_15_SECONDS"


	def main(args : Array[String]) {
		val scheduler = StdSchedulerFactory.getDefaultScheduler()
		scheduler.start()

		val dbsession = HibernateTools.getLightweightSession()
		val msgsession = MessagingTools.getEmbeddedSession()

		val datamap = new JobDataMap()
		datamap.put(InjectorServiceJob.DATABASE_SESSION, dbsession)
		datamap.put(InjectorServiceJob.MESSAGING_SESSION, msgsession)

		val job = new JobDetail(JOB, JOB_GROUP, classOf[InjectorServiceJob])
		job.setJobDataMap(datamap)

		val trigger = new CronTrigger(JOB, JOB_GROUP, "0/15 * * * * ?")

		scheduler.scheduleJob(job, trigger)
	}
}
