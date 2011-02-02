
package axirassa.services.runners;

import org.hibernate.Session;
import org.hornetq.api.core.client.ClientSession;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import axirassa.util.HibernateTools;
import axirassa.util.MessagingTools;

public class ControllerServiceRunner {
	public static String JOB_GROUP = "AxController";





	enum ControllerJobs {
		EVERY_MINUTE;
	}


	public static void main(String[] args) throws Exception {
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		scheduler.start();

		Session databaseSession = HibernateTools.getSession();
		ClientSession messagingSession = MessagingTools.getEmbeddedSession();

		JobDataMap datamap = new JobDataMap();
		datamap.put(ControllerServiceJob.DATABASE_SESSION, databaseSession);
		datamap.put(ControllerServiceJob.MESSAGING_SESSION, messagingSession);

		JobDetail job = new JobDetail(ControllerJobs.EVERY_MINUTE.toString(), JOB_GROUP, ControllerServiceJob.class);
		job.setJobDataMap(datamap);

		Trigger trigger = new CronTrigger(ControllerJobs.EVERY_MINUTE.toString(), JOB_GROUP, "0 * * * * ?");

		scheduler.scheduleJob(job, trigger);
		return;
	}
}
