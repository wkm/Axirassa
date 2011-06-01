
package test.axirassa.util;

import static org.junit.Assert.assertTrue;

import java.text.ParseException;

import org.junit.Test;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

public class TestQuartz {
	@Test
	public void test() throws SchedulerException, ParseException, InterruptedException {
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		scheduler.start();

		JobDetail job = new JobDetail("job1", "group1", QuartzTestJobClass.class);
		Trigger trigger = new CronTrigger("trigger1", "group1", "0/1 * * * * ?");

		scheduler.scheduleJob(job, trigger);

		Thread.sleep(50);
		scheduler.shutdown(true);

		assertTrue(QuartzTestJobClass.flipped);
	}
}
