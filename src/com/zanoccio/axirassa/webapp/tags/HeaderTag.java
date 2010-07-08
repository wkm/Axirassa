
package com.zanoccio.axirassa.webapp.tags;

import java.io.IOException;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspWriter;

public class HeaderTag extends WebappTag {
	@Override
	public void doTagExecute() throws IOException {
		JspContext pageContext = getJspContext();
		JspWriter out = pageContext.getOut();

		out.println("<div id='header'>");
		out.println("<div id='title'>axirassa | pinger</div>");
		out.println("<div id='subtitle'>[ technology prototype ]</div>");
		out.println("</div>");
	}
}
