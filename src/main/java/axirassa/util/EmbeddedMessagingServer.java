

package axirassa.util;

import java.io.IOException;import java.util.HashSet;import java.util.concurrent.ScheduledThreadPoolExecutor;import java.util.concurrent.TimeUnit;import lombok.extern.slf4j.Slf4j;import org.hornetq.api.core.HornetQException;import org.hornetq.api.core.SimpleString;import org.hornetq.api.core.TransportConfiguration;import org.hornetq.api.core.client.ClientConsumer;import org.hornetq.api.core.client.ClientMessage;import org.hornetq.api.core.client.ClientSession;import org.hornetq.api.core.client.ClientSession.QueueQuery;import org.hornetq.api.core.management.HornetQServerControl;import org.hornetq.core.config.impl.FileConfiguration;import org.hornetq.core.remoting.impl.invm.InVMAcceptorFactory;import org.hornetq.core.remoting.impl.netty.NettyAcceptorFactory;import org.hornetq.core.server.HornetQServer;import org.hornetq.core.server.HornetQServers;import org.hornetq.core.server.JournalType;import axirassa.config.Messaging;import axirassa.model.PingerEntity;
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
	private final HornetQServerControl serverControl;	private final ClientSession session;
	private int printCount;


	public ServerQueueLister(final HornetQServer server) throws Exception {
		this.session = MessagingTools.getEmbeddedSession();
		this.serverControl = server.getHornetQServerControl();
	}


	@Override
	public void run() {
		try {
			String[] queues = serverControl.getQueueNames();
			System.out.println("QUEUES:");
			for (String queue : queues) {	
				QueueQuery query = session.queueQuery(new SimpleString(queue));
				System.out.printf("\t%50s MSGS Q'D: %5d   CONSUMERS: %3d   \n", queue,
				                  query.getMessageCount(), query.getConsumerCount());
				
				if(queue.equalsIgnoreCase(Messaging.PINGER_REQUEST_QUEUE))
					printPingersOnQueue(queue);
			}
		} catch (Exception e) {
			log.error("Exception: ", e);
		}
	}


	private void printPingersOnQueue(String queue) throws HornetQException, IOException, ClassNotFoundException {
		printCount = 0;
		
		ClientConsumer queueBrowser = session.createConsumer(queue, true);
		ClientMessage message = null;
		while((message = queueBrowser.receiveImmediate()) != null) {
			byte[] buffer = new byte[message.getBodyBuffer().readableBytes()];
			message.getBodyBuffer().readBytes(buffer);
			Object rawobject = AutoSerializingObject.fromBytes(buffer);
			
			if(rawobject instanceof PingerEntity) {				
				PingerEntity entity = (PingerEntity) rawobject;
				print(entity.getUrl());
			} else
				print("<unknown>");
		}
		System.out.printf("\n");
    }
	
	private void print(String msg) {
		printCount++;
		if(printCount % 5 == 0)
			System.out.printf("\n");
		
		System.out.printf("%30s  ", msg);
	}
}
