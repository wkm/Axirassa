
package axirassa.services.runners;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import axirassa.ioc.IocStandalone;
import axirassa.services.DatabaseCleanerService;
import axirassa.services.Service;

public class DatabaseCleanerServiceJob implements Job {
	@Override
	public void execute (JobExecutionContext context) throws JobExecutionException {
		try {
			Service service = IocStandalone.autobuild(DatabaseCleanerService.class);
			service.execute();
		} catch (Throwable e) {
			throw new JobExecutionException(e);
		}
	}
}
