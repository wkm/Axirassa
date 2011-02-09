
package axirassa.webapp.ajax;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class DataStreamSessionMap {
	private final HashMap<String, Long> sessionMap = new HashMap<String, Long>();
	private final HashMap<Long, HashSet<String>> pingerMap = new HashMap<Long, HashSet<String>>();


	public void addSessionPinger(long pinger, String session) {
		System.err.println("Subscribing session: " + session + " to pinger: " + pinger);
		sessionMap.put(session, pinger);

		if (pingerMap.get(pinger) == null)
			pingerMap.put(pinger, new HashSet<String>());

		pingerMap.get(pinger).add(session);
	}


	public Set<String> getSessions(long pinger) {
		return pingerMap.get(pinger);
	}


	public Long getPinger(String session) {
		return sessionMap.get(session);
	}


	public boolean hasSession(String session) {
		return getPinger(session) != null;
	}


	public void removeBySession(String session) {
		Long pinger = getPinger(session);

		// no such session
		if (pinger == null)
			return;

		sessionMap.remove(session);
		HashSet<String> pingerSession = pingerMap.get(pinger);
		if (pingerSession == null)
			// shouldn't happen
			return;

		pingerSession.remove(session);

		// remove the session set if there are no sessions left
		if (pingerSession.size() == 0)
			pingerMap.remove(pinger);
	}
}
