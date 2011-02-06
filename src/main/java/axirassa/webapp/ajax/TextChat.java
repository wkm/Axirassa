
package axirassa.webapp.ajax;

import java.util.ArrayList;
import java.util.Collection;

import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.proxy.dwr.Util;
import org.directwebremoting.util.Logger;

public class TextChat {
	protected static final Logger log = Logger.getLogger(TextChat.class);

	private final ArrayList<TextChatMessage> messages = new ArrayList<TextChatMessage>();


	public void addMessage(String text) {
		if (text != null && text.trim().length() > 0)
			messages.add(0, new TextChatMessage(text));

		WebContext wctx = WebContextFactory.get();
		String currentPage = wctx.getCurrentPage();

		Util utilThis = new Util(wctx.getScriptSession());
		utilThis.setValue("text", "");

		Collection<ScriptSession> session = wctx.getScriptSessionsByPage(currentPage);
		Util utilAll = new Util(session);

		utilAll.removeAllOptions("chatlog");
		utilAll.addOptions("chatlog", messages, "text");
	}

}
