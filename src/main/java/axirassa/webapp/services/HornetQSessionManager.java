
package axirassa.webapp.services;

import org.apache.tapestry5.ioc.services.ThreadCleanupListener;
import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.client.ClientSession;

import axirassa.util.MessagingTools;

public class HornetQSessionManager implements ThreadCleanupListener {

	private final ClientSession session;


	public HornetQSessionManager() throws HornetQException {
		session = MessagingTools.getEmbeddedSession();
	}


	@Override
	public void threadDidCleanup() {
		try {
			session.close();
		} catch (HornetQException e) {
			e.printStackTrace();
		}
	}
}
