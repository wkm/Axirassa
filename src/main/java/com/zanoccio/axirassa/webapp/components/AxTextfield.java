
package com.zanoccio.axirassa.webapp.components;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.CleanupRender;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.TextField;

public class AxTextfield extends TextField {

	@SetupRender
	void beforeRenderTemplate(MarkupWriter writer) {
		writer.writeRaw("<td>");
	}


	@CleanupRender
	void afterRenderTemplate(MarkupWriter writer) {
		writer.writeRaw("</td>");
	}
}
