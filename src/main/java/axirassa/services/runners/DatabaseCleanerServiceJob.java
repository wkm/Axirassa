package axirassa.services.runners;


import axirassa.services.DatabaseCleanerService;
import axirassa.services.Service;
import axirassa.util.HibernateTools;
import org.hibernate.Session;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


public class DatabaseCleanerServiceJob implements Job {
	@Override
	public void execute (JobExecutionContext context) throws JobExecutionException {
		try {
			Session database = HibernateTools.getLightweightSession();
			Service service = new DatabaseCleanerService(database);

			service.execute();
		} catch (Exception e) {
			throw new JobExecutionException(e);
		}
	}
}
