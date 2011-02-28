
package axirassa.webapp.services;

import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientProducer;
import org.hornetq.api.core.client.ClientSession;

public interface MessagingSession {
	public ClientSession getSession();


	public ClientProducer createProducer() throws HornetQException;


	public ClientProducer createProducer(String queue) throws HornetQException;


	public ClientMessage createMessage(boolean durable);


	public void close() throws HornetQException;
}
