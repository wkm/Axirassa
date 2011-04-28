
package axirassa.services.runners;

import org.hibernate.Session;
import org.hornetq.api.core.client.ClientSession;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import axirassa.services.InjectorService;
import axirassa.services.Service;

public class InjectorServiceJob implements Job {
	public static String DATABASE_SESSION = "session.database";
	public static String MESSAGING_SESSION = "session.messaging";


	@Override
	public void execute (JobExecutionContext context) throws JobExecutionException {
		try {
			JobDataMap datamap = context.getJobDetail().getJobDataMap();

			ClientSession messagingSession = (ClientSession) datamap.get(MESSAGING_SESSION);
			Session databaseSession = (Session) datamap.get(DATABASE_SESSION);

			Service service = new InjectorService(messagingSession, databaseSession);
			service.execute();
		} catch (Exception e) {
			throw new JobExecutionException(e);
		}
	}
}
