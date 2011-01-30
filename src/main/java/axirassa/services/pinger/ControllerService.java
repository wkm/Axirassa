
package axirassa.services.pinger;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientProducer;
import org.hornetq.api.core.client.ClientSession;

import axirassa.config.Messaging;
import axirassa.messaging.PingerRequestMessage;
import axirassa.model.PingerModel;
import axirassa.services.Service;
import axirassa.util.EmbeddedMessagingServer;
import axirassa.util.HibernateTools;
import axirassa.util.MessagingTools;

/**
 * The ControllerService executes every minute, creating a message for each
 * triggered pinger.
 * 
 * @author wiktor
 */
public class ControllerService implements Service {

	private final ClientSession messagingSession;
	private final Session databaseSession;


	public ControllerService(ClientSession messagingSession, Session databaseSession) {
		this.messagingSession = messagingSession;
		this.databaseSession = databaseSession;
	}


	@Override
	public void execute() throws Exception {
		ClientProducer producer = messagingSession.createProducer(Messaging.PINGER_REQUEST_QUEUE);

		Query query = databaseSession.createQuery("from PingerModel where frequency=:frequency");
		query.setInteger("frequency", 3600);

		List<PingerModel> pingers = query.list();
		long start = System.currentTimeMillis();
		int msgcount = 0;
		for (int i = 0; i < 300; i++) {
			for (PingerModel pinger : pingers) {
				ClientMessage message = messagingSession.createMessage(false);
				message.getBodyBuffer().writeBytes(new PingerRequestMessage(pinger).toBytes());
				producer.send(message);

				msgcount++;
			}
		}
		System.out.println("Sent " + msgcount + " messages in: " + (System.currentTimeMillis() - start) + "ms");

		producer.close();
	}


	public static void main(String[] args) throws Exception {
		EmbeddedMessagingServer.start();

		ClientSession msgsession = MessagingTools.getEmbeddedSession();
		Session dbsession = HibernateTools.getSession();

		Service service = new ControllerService(msgsession, dbsession);

		System.out.println("Executing injector");
		service.execute();

		return;
	}
}
