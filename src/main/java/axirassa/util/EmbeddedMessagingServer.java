
package axirassa.util;

import java.util.HashSet;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

import org.hornetq.api.core.SimpleString;
import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.core.client.ClientSession.QueueQuery;
import org.hornetq.api.core.management.HornetQServerControl;
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
	private static HornetQServer server;


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

		server = HornetQServers.newHornetQServer(config);
		server.start();

		System.out.println("Axirassa Embedded HornetQ server started.");

		ServerQueueLister lister = new ServerQueueLister(server);

		ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
		executor.scheduleAtFixedRate(lister, 1, 15, TimeUnit.SECONDS);
	}


	static public void stop() throws Exception {
		server.stop();
	}


	static public void main(String[] args) throws Exception {
		start();
	}
}

@Slf4j
class ServerQueueLister implements Runnable {
	private final HornetQServerControl serverControl;


	public ServerQueueLister(final HornetQServer server) {
		this.serverControl = server.getHornetQServerControl();
	}


	@Override
	public void run() {
		try {
			String[] queues = serverControl.getQueueNames();
			System.out.println("QUEUES:");
			for (String queue : queues) {	
				QueueQuery query = MessagingTools.getEmbeddedSession().queueQuery(new SimpleString(queue));

				System.out.printf("\t%40s DURABLE: %b MSGS Q'D: %5d   CONSUMERS: %3d   \n", queue, query.isDurable(),
				                  query.getMessageCount(), query.getConsumerCount());
			}
		} catch (Exception e) {
			log.error("Exception: ", e);
		}
	}
}
