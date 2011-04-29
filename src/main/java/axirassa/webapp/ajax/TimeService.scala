
package axirassa.webapp.ajax;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;

import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.bayeux.server.ConfigurableServerChannel;
import org.cometd.bayeux.server.ServerChannel;
import org.cometd.bayeux.server.ServerSession;
import org.cometd.server.AbstractService;

public class TimeService extends AbstractService {

	ServerChannel channel;

	public static HashSet<ServerSession> subscribers;


	public TimeService (BayeuxServer server) throws InterruptedException {
		super(server, "timeService");

		ServerChannel.Initializer initalizer = new ServerChannel.Initializer() {
			@Override
			public void configureChannel (ConfigurableServerChannel channel) {
				System.out.println("Configuring channel");
				channel.setPersistent(true);
			}
		};

		System.err.println("####### INITIALIZING TIME SERVICE");
		startTimeFilter();
	}


	private void startTimeFilter () {
		System.err.println("FORKING TIME FILTER THREAD");
		final ClientSessionChannel channel1 = getLocalSession().getChannel("/ax/timeplease");
		final ClientSessionChannel channel2 = getLocalSession().getChannel("/ax/valueplease");
		// final ClientSessionChannel channel3 =
		// getLocalSession().getChannel("/ax/pinger/6351");
		final DateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SS z");
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run () {
				while (true) {
					channel1.publish(format.format(new Date()));
					channel2.publish(new Random().nextDouble());
					// channel3.publish(new Random().nextDouble());
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
}
