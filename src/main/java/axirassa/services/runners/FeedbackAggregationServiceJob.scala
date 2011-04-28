
package axirassa.services.runners;

import axirassa.ioc.IocStandalone;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import axirassa.services.FeedbackAggregationService;
import axirassa.services.Service;

public class FeedbackAggregationServiceJob implements Job {

	public static final String DATABASE_SESSION = "session.database";
	public static final String MESSAGING_SESSION = "session.messaging";


	@Override
	public void execute (JobExecutionContext context) throws JobExecutionException {
		try {
			JobDataMap datamap = context.getJobDetail().getJobDataMap();
            // TODO use datamap to populate services for injection
            Service service = IocStandalone.autobuild(FeedbackAggregationService.class);
			service.execute();
		} catch (Exception e) {
			throw new JobExecutionException(e);
		}
	}
}
