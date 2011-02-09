
package axirassa.webapp.ajax.util;

import org.directwebremoting.event.ScriptSessionEvent;
import org.directwebremoting.event.ScriptSessionListener;

import axirassa.webapp.ajax.PingerLevelDataStream;

public class StreamSessionListener implements ScriptSessionListener {

	@Override
	public void sessionCreated(ScriptSessionEvent ev) {
	}


	@Override
	public void sessionDestroyed(ScriptSessionEvent ev) {
		String sessionId = ev.getSession().getId();
		PingerLevelDataStream.getInstance().unsubscribe(sessionId);
	}
}
