
package axirassa.webapp.ajax;

import java.util.Date;
import java.util.HashSet;

import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.bayeux.server.ConfigurableServerChannel;
import org.cometd.bayeux.server.ServerChannel;
import org.cometd.bayeux.server.ServerMessage;
import org.cometd.bayeux.server.ServerSession;
import org.cometd.server.AbstractService;

public class TimeService extends AbstractService {

	ServerChannel channel;

	public static HashSet<ServerSession> subscribers;


	public TimeService(BayeuxServer server) throws InterruptedException {
		super(server, "timeService");

		ServerChannel.Initializer initalizer = new ServerChannel.Initializer() {
			@Override
			public void configureChannel(ConfigurableServerChannel channel) {
				System.out.println("Configuring channel");
				channel.setPersistent(true);
			}
		};

		addService("/ax/timeplease", "time");

		System.err.println("####### INITIALIZING TIME SERVICE");
		startTimeFilter();
	}


	private void startTimeFilter() {
		System.err.println("FORKING TIME FILTER THREAD");
		final ClientSessionChannel channel = getLocalSession().getChannel("/ax/timeplease");
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					System.err.println("PUBLISHING DATE");
					channel.publish(new Date());
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});

		thread.start();
	}


	public void time(ServerSession remote, ServerMessage message) {
		System.err.println("####### CALLING TIME SERVICE");
		System.err.println("RECEIVED MESSAGE: " + message);
		send(remote, "/ax/timeplease", "time is now 2", null);
		remote.deliver(getServerSession(), "/ax/timeplease", new int[] { 12, 13, 14 }, null);
		ClientSessionChannel channel = getLocalSession().getChannel("/ax/timeplease");
		channel.publish(new Date());
	}
}
