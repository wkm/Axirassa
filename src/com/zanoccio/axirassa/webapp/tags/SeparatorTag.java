
package com.zanoccio.axirassa.webapp.tags;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspWriter;

public class SeparatorTag extends WebappTag {

	@Override
	public void doTagExecute() throws Exception {
		JspContext context = getJspContext();
		JspWriter out = context.getOut();

		out.println("<div class='sep'>&nbsp;</div>");
	}

}
