
package axirassa.services.runners;

import org.hibernate.Session;
import org.hornetq.api.core.client.ClientSession;

import axirassa.services.Service;
import axirassa.services.pinger.ControllerService;
import axirassa.util.HibernateTools;
import axirassa.util.MessagingTools;

public class ControllerServiceRunner {
	public static void main(String[] args) throws Exception {
		ClientSession msgsession = MessagingTools.getEmbeddedSession();
		Session dbsession = HibernateTools.getSession();

		Service service = new ControllerService(msgsession, dbsession);

		System.out.println("Executing injector");
		service.execute();
		System.out.println("Finished executing");

		return;
	}
}
