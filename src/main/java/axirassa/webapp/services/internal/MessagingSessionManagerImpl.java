
package axirassa.webapp.services.internal;

import org.hornetq.api.core.HornetQException;

import axirassa.util.MessagingTools;
import axirassa.webapp.services.MessagingSession;
import axirassa.webapp.services.MessagingSessionManager;

public class MessagingSessionManagerImpl implements MessagingSessionManager {

	private final MessagingSession session;


	public MessagingSessionManagerImpl() throws HornetQException {
		System.err.println("CREATING SESSION MANAGER AND SESSION");
		session = new MessagingSessionImpl(MessagingTools.getEmbeddedSession());
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see axirassa.webapp.services.MessagingSessionManager#getSession()
	 */
	@Override
	public MessagingSession getSession() {
		return session;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see axirassa.webapp.services.MessagingSessionManager#threadDidCleanup()
	 */
	@Override
	public void threadDidCleanup() {
		try {
			System.err.println("CLEANING UP AFTER THREAD");
			session.close();
		} catch (HornetQException e) {
			e.printStackTrace();
		}
	}
}
