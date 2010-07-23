
package com.zanoccio.axirassa.util;

import java.util.HashSet;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.core.config.Configuration;
import org.hornetq.core.config.impl.ConfigurationImpl;
import org.hornetq.core.remoting.impl.invm.InVMAcceptorFactory;
import org.hornetq.core.server.HornetQServer;
import org.hornetq.core.server.impl.HornetQServerImpl;

/**
 * Starts HornetQ as an embedded messaging server.
 * 
 * @author wiktor
 */
public class EmbeddedMessagingServer {
	static public void start() throws Exception {
		Configuration config = new ConfigurationImpl();

		HashSet<TransportConfiguration> transports = new HashSet<TransportConfiguration>();
		transports.add(new TransportConfiguration(InVMAcceptorFactory.class.getName()));

		config.setAcceptorConfigurations(transports);

		HornetQServer server = new HornetQServerImpl(config);
		server.start();

		System.out.println("HornetQ server started");
	}
}
