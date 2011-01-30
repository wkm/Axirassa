
package test.axirassa.util;

import static org.junit.Assert.assertTrue;

import java.text.ParseException;

import org.junit.Test;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

public class TestQuartz {
	@Test
	public void test() throws SchedulerException, ParseException, InterruptedException {
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		scheduler.start();

		JobDetail job = new JobDetail("job1", "group1", TestJobClass.class);
		Trigger trigger = new CronTrigger("trigger1", "group1", "/1 * * * * *");

		scheduler.scheduleJob(job, trigger);
		Thread.sleep(30L * 1000L);
		scheduler.shutdown(true);

		assertTrue(TestJobClass.flipped);
	}
}

class TestJobClass implements Job {

	static public boolean flipped = false;


	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("Job executed.");
		flipped = true;
	}
}
