
package axirassa.webapp.ajax;

import java.io.IOException;
import java.util.Date;
import java.util.Set;

import org.apache.log4j.Logger;
import org.directwebremoting.Browser;
import org.directwebremoting.ScriptSession;
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

	private transient DataStreamSessionMap pingerMap = new DataStreamSessionMap();


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
		ScriptSession session = WebContextFactory.get().getScriptSession();
		log.info("Subscribing to " + pingerId + " from :" + session.getId());
		pingerMap.addSessionPinger(pingerId, session.getId());
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
				ClientMessage message = consumer.receive();
				HttpStatisticsEntity stat = InjectorService.rebuildMessage(message);
				if (stat == null) {
					log.warn("received null message");
					continue;
				}

				PingerEntity pinger = stat.getPinger();

				Set<String> scriptingSessions = pingerMap.getSessions(pinger.getId());
				if (scriptingSessions == null)
					continue;
				else
					for (String scriptingSession : scriptingSessions) {
						log.info("#### Pushing: " + stat.getResponseTime() + " to: " + scriptingSession);
						streamData(scriptingSession, stat.getTimestamp(), stat.getResponseTime(),
						           stat.getResponseSize());
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


	private void streamData(String scriptingSession, final Date date, final int responseTime, final long responseSize) {
		// ScriptSession session =
		// ServerContextFactory.get().getScriptSessionById(scriptingSession);
		// session.

		Browser.withSession(scriptingSession, new Runnable() {
			@Override
			public void run() {
				ScriptSessions.addFunctionCall("addDataPoint", date, responseTime, responseSize);
			}
		});

		// // TODO this should use Browser.session(...) but there's a bug in the
		// // implementation
		// ScriptSession session =
		// ServerContextFactory.get().getScriptSessionById(scriptingSession);
		// session.addScript(new ScriptBuffer("addDataPoint(" + responseTime +
		// ")"));
	}
}
