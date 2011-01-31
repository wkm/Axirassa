
package test.axirassa.util;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class TestQuartzJobClass implements Job {
	static public boolean flipped = false;


	public TestQuartzJobClass() {
	}


	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("Job executed.");
		flipped = true;
	}
}
