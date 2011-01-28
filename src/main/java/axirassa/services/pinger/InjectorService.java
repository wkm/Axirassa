
package axirassa.services.pinger;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hornetq.api.core.client.ClientSession;

import axirassa.services.Service;

/**
 * The InjectorService executes every minute, creating a message for each
 * triggered pinger.
 * 
 * @author wiktor
 */
public class InjectorService implements Service {

	private final ClientSession messagingSession;
	private final Session databaseSession;


	public InjectorService(ClientSession messagingSession, Session databaseSession) {
		this.messagingSession = messagingSession;
		this.databaseSession = databaseSession;
	}


	@Override
	public void execute() throws Exception {
		Query query = databaseSession.createQuery("from PingerModel where interval=:interval");

	}
}
