
package axirassa.services.runners;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import axirassa.ioc.IocStandalone;
import axirassa.services.ControllerService;
import axirassa.services.Service;

public class ControllerServiceJob implements Job {
	@Override
	public void execute (JobExecutionContext context) throws JobExecutionException {
		try {
			Service service = IocStandalone.autobuild(ControllerService.class);
			service.execute();
		} catch (Exception e) {
			throw new JobExecutionException(e);
		}
	}
}
