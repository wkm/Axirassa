
package axirassa.services.runners;

import org.hibernate.Session;
import org.hornetq.api.core.client.ClientSession;

import axirassa.services.ControllerService;
import axirassa.services.Service;
import axirassa.util.HibernateTools;
import axirassa.util.MessagingTools;

public class ControllerServiceRunner {
	public static void main(String[] args) throws Exception {
		ClientSession msgsession = MessagingTools.getEmbeddedSession();
		Session dbsession = HibernateTools.getSession();

		Service service = new ControllerService(msgsession, dbsession);

		System.out.println("Executing controller");
		service.execute();
		System.out.println("Finished executing");

		return;
	}
}
