
package axirassa.webapp.services.internal;

import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientProducer;
import org.hornetq.api.core.client.ClientSession;

import axirassa.webapp.services.MessagingSession;

public class MessagingSessionImpl implements MessagingSession {

	private final ClientSession session;


	public MessagingSessionImpl(ClientSession session) {
		this.session = session;
	}


	@Override
	public ClientProducer createProducer(String queue) throws HornetQException {
		return session.createProducer(queue);
	}


	@Override
	public ClientMessage createMessage(boolean durable) {
		return session.createMessage(durable);
	}


	@Override
	public ClientSession getSession() {
		return session;
	}


	@Override
	public void close() throws HornetQException {
		session.close();
	}


	@Override
	public ClientProducer createProducer() throws HornetQException {
		return session.createProducer();
	}

}
