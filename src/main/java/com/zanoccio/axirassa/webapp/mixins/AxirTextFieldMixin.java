
package com.zanoccio.axirassa.webapp.mixins;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;

public class AxirTextFieldMixin {
	@BeginRender
	void beforeRenderTemplate(MarkupWriter writer) {
		System.out.println("Calling mixin");
		writer.writeRaw("<td>");
	}


	@AfterRender
	void afterRenderTemplate(MarkupWriter writer) {
		writer.writeRaw("</td>");
	}

}
