
package axirassa.util;

import java.io.IOException;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientSession;
import org.hornetq.api.core.client.ClientSessionFactory;
import org.hornetq.api.core.client.HornetQClient;
import org.hornetq.api.core.client.ServerLocator;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;

import axirassa.services.exceptions.InvalidMessageClassException;

public class MessagingTools {
	public static ClientSession getEmbeddedSession() throws Exception {
		TransportConfiguration configuration = new TransportConfiguration(NettyConnectorFactory.class.getName());
		ServerLocator locator = HornetQClient.createServerLocatorWithoutHA(configuration);
		ClientSessionFactory factory = locator.createSessionFactory();
		ClientSession session = factory.createSession();
		return session;
	}


	public static byte[] bodyBytes(ClientMessage message) {
		byte[] buffer = new byte[message.getBodyBuffer().readableBytes()];
		message.getBodyBuffer().readBytes(buffer);
		return buffer;
	}


	public static Object fromMessageBytes(ClientMessage message) throws IOException, ClassNotFoundException {
		return AutoSerializingObject.fromBytes(bodyBytes(message));
	}
	
	public static <T> T fromMessageBytes(Class<? extends T> messageClass, ClientMessage message) throws IOException, ClassNotFoundException, InvalidMessageClassException {
		if(message == null)
			return null;
		
		Object rawobject = fromMessageBytes(message);
		if(messageClass.isInstance(rawobject))
			return (T) rawobject;
		else
			throw new InvalidMessageClassException(messageClass, rawobject);
	}
}
