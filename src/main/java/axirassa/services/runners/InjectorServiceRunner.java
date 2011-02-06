
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

public class InjectorServiceRunner {
	public static String JOB_GROUP = "AxInjector";
	public static String JOB = "EVERY_15_SECONDS";


	public static void main(String[] args) throws Exception {
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		scheduler.start();

		Session dbsession = HibernateTools.getLightweightSession();
		ClientSession msgsession = MessagingTools.getEmbeddedSession();

		JobDataMap datamap = new JobDataMap();
		datamap.put(InjectorServiceJob.DATABASE_SESSION, dbsession);
		datamap.put(InjectorServiceJob.MESSAGING_SESSION, msgsession);

		JobDetail job = new JobDetail(JOB, JOB_GROUP, InjectorServiceJob.class);
		job.setJobDataMap(datamap);

		Trigger trigger = new CronTrigger(JOB, JOB_GROUP, "0/15 * * * * ?");

		scheduler.scheduleJob(job, trigger);
		return;
	}
}
