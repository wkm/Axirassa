
package axirassa.webapp.ajax;

import org.directwebremoting.Security;

public class TextChatMessage {
	private final String text;
	private final long id = System.currentTimeMillis();


	public TextChatMessage(String rawText) {
		String text = rawText;

		if (text.length() > 256)
			text = text.substring(0, 256);

		text = Security.replaceXmlCharacters(text);

		this.text = text;
	}


	public long getId() {
		return id;
	}


	public String getText() {
		return text;
	}
}
