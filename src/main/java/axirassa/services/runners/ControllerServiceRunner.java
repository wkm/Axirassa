
package axirassa.services.runners;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

public class ControllerServiceRunner {
	public static String JOB_GROUP = "AxController";





	enum ControllerJobs {
		EVERY_MINUTE;
	}


	public static void main (String[] args) throws Exception {
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		scheduler.start();

		JobDetail job = new JobDetail(ControllerJobs.EVERY_MINUTE.toString(), JOB_GROUP, ControllerServiceJob.class);
		Trigger trigger = new CronTrigger(ControllerJobs.EVERY_MINUTE.toString(), JOB_GROUP, "0/60 * * * * ?");

		scheduler.scheduleJob(job, trigger);
		return;
	}
}
