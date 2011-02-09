
package axirassa.webapp.ajax;

import java.util.HashMap;

public class PingerSessionMap {
	private final HashMap<String, Long> sessionMap = new HashMap<String, Long>();
	private final HashMap<Long, String> pingerMap = new HashMap<Long, String>();


	public void addSessionPinger(long pinger, String session) {
		System.err.println("Subscribing session: " + session + " to pinger: " + pinger);
		sessionMap.put(session, pinger);
		pingerMap.put(pinger, session);
	}


	public String getSession(long pinger) {
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
		pingerMap.remove(pinger);
	}
}
