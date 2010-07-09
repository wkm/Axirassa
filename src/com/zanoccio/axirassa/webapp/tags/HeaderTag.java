
package com.zanoccio.axirassa.webapp.tags;

import java.io.IOException;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

public class HeaderTag extends WebappTag {
	@Override
	public void doTagExecute() throws IOException, JspException {
		JspContext pageContext = getJspContext();
		JspWriter out = pageContext.getOut();

		StringBuffer sb = new StringBuffer();

		sb.append("<div id='header'>");
		sb.append("<div id='title'>axirassa | pinger</div>");
		sb.append("<div id='subtitle'>[ technology prototype ]</div>");
		sb.append("</div>");

		out.print(sb);
	}
}
