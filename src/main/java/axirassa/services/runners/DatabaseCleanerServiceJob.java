
package axirassa.services.runners;

import org.hibernate.Session;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import axirassa.services.DatabaseCleanerService;
import axirassa.services.Service;
import axirassa.util.HibernateTools;

public class DatabaseCleanerServiceJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			Session dbSession = HibernateTools.getLightweightSession();
			Service service = new DatabaseCleanerService(dbSession);

			service.execute();
		} catch (Exception e) {
			throw new JobExecutionException(e);
		}
	}

}
