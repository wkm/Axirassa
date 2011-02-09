
package axirassa.util;

import java.util.HashSet;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.core.config.impl.FileConfiguration;
import org.hornetq.core.remoting.impl.invm.InVMAcceptorFactory;
import org.hornetq.core.remoting.impl.netty.NettyAcceptorFactory;
import org.hornetq.core.server.HornetQServer;
import org.hornetq.core.server.HornetQServers;
import org.hornetq.core.server.JournalType;

/**
 * Starts HornetQ as an embedded messaging server.
 * 
 * @author wiktor
 */
public class EmbeddedMessagingServer {
	static public void start() throws Exception {
		FileConfiguration config = new FileConfiguration();

		config.setConfigurationUrl("hornetq-configuration.xml");

		HashSet<TransportConfiguration> transports = new HashSet<TransportConfiguration>();
		transports.add(new TransportConfiguration(NettyAcceptorFactory.class.getName()));
		transports.add(new TransportConfiguration(InVMAcceptorFactory.class.getName()));

		config.setJournalType(JournalType.NIO);
		config.setAcceptorConfigurations(transports);
		config.setSecurityEnabled(false);

		config.start();

		HornetQServer server = HornetQServers.newHornetQServer(config);
		server.start();

		System.out.println("Axirassa Embedded HornetQ server started.");

		ServerQueueLister lister = new ServerQueueLister(server);

		ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
		executor.scheduleAtFixedRate(lister, 1, 1, TimeUnit.MINUTES);
	}


	static public void main(String[] args) throws Exception {
		start();
	}
}

class ServerQueueLister implements Runnable {

	private final HornetQServer server;


	public ServerQueueLister(final HornetQServer server) {
		this.server = server;
	}


	@Override
	public void run() {
		String[] queues = server.getHornetQServerControl().getQueueNames();
		System.out.println("QUEUES:");
		for (String queue : queues)
			System.out.println("\t" + queue);
	}
}
