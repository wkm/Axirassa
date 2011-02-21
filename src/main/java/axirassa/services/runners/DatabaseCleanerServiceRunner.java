
package axirassa.services.runners;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

public class DatabaseCleanerServiceRunner {
	public static String JOB_GROUP = "AxDatabaseCleaner";
	public static String JOB = "EVERY_HOUR";


	public static void main(String[] args) throws Exception {
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		scheduler.start();

		JobDetail job = new JobDetail(JOB, JOB_GROUP, DatabaseCleanerServiceJob.class);
		Trigger trigger = new CronTrigger(JOB, JOB_GROUP, "0 * * * * ?");
		scheduler.scheduleJob(job, trigger);
		return;
	}
}
