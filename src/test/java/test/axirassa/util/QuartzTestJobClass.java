
package test.axirassa.util;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class QuartzTestJobClass implements Job {
	static public boolean flipped = false;


	public QuartzTestJobClass() {
		// empty
	}


	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("Job executed.");
		flipped = true;
	}
}
