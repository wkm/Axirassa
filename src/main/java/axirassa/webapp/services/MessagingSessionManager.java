
package axirassa.webapp.services;

import org.apache.tapestry5.ioc.services.ThreadCleanupListener;

public interface MessagingSessionManager extends ThreadCleanupListener {

	public abstract MessagingSession getSession();


	public abstract void threadDidCleanup();

}
