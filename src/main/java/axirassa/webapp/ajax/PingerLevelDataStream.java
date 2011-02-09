
package axirassa.webapp.ajax;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.directwebremoting.Browser;
import org.directwebremoting.ScriptSessions;
import org.directwebremoting.WebContextFactory;
import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientSession;

import axirassa.model.HttpStatisticsEntity;
import axirassa.model.PingerEntity;
import axirassa.services.InjectorService;
import axirassa.services.exceptions.InvalidMessageClassException;
import axirassa.util.MessagingTools;
import axirassa.util.MessagingTopic;
import axirassa.webapp.ajax.util.DaemonThreadFactory;

public class PingerLevelDataStream implements Runnable {
	protected static final Logger log = Logger.getRootLogger();
	private static PingerLevelDataStream instance;

	private transient PingerSessionMap pingerMap = new PingerSessionMap();


	public static PingerLevelDataStream getInstance() {
		if (instance == null)
			instance = new PingerLevelDataStream();

		return instance;
	}


	private PingerLevelDataStream() {
		DaemonThreadFactory threadFactory = new DaemonThreadFactory();
		Thread t = threadFactory.newThread(this);
		t.start();
	}


	public synchronized void subscribe(long pingerId) {
		String sessionId = WebContextFactory.get().getScriptSession().getId();
		log.info("Subscribing to " + pingerId + " from :" + sessionId);
		pingerMap.addSessionPinger(pingerId, sessionId);
	}


	public synchronized void unsubscribe(String session) {
		log.info("Unsubscribing from " + session);
		pingerMap.removeBySession(session);
	}


	@Override
	public void run() {
		ClientConsumer consumer = null;
		try {
			ClientSession session = MessagingTools.getEmbeddedSession();
			MessagingTopic topic = new MessagingTopic(session, "ax.account.#");
			consumer = topic.createConsumer();
			session.start();
		} catch (HornetQException e) {
			log.error("Could not run PingerLevelDataStream ", e);
			return;
		}

		log.info("Initializing PingerLevelDataStream");
		while (true) {
			try {
				log.info("#### awaiting message");
				ClientMessage message = consumer.receive();
				log.info("#### message received: " + message);
				HttpStatisticsEntity stat = InjectorService.rebuildMessage(message);
				PingerEntity pinger = stat.getPinger();

				log.info("#### Pinger: " + pinger);

				String scriptingSession = pingerMap.getSession(pinger.getId());
				if (scriptingSession == null)
					log.info("Ignoring HttpStatisticsEntity to unsubscribed pinger");
				else {
					log.info("Sending response time to " + scriptingSession);
					streamData(scriptingSession, stat.getResponseTime());
				}
			} catch (InvalidMessageClassException e) {
				log.error("Exception:", e);
			} catch (HornetQException e) {
				log.error("Exception:", e);
			} catch (IOException e) {
				log.error("Exception:", e);
			} catch (ClassNotFoundException e) {
				log.error("Exception:", e);
			}
		}
	}


	private void streamData(String scriptingSession, final int responseTime) {
		Browser.withSession(scriptingSession, new Runnable() {
			@Override
			public void run() {
				ScriptSessions.addFunctionCall("addDataPoint", responseTime);
			}
		});
	}
}
