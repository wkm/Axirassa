
package com.zanoccio.axirassa.webapp.tags;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.JspFragment;

public class HeadTag extends WebappTag {

	@Override
	public void doTagExecute() throws IOException, JspException {
		JspFragment body = getJspBody();
		JspContext context = getJspContext();
		JspWriter out = context.getOut();

		StringWriter writer = new StringWriter();
		StringBuffer buffer = writer.getBuffer();

		buffer.append("<head>");
		buffer.append("<link rel='stylesheet' type='text/css' href='/axir/CSS/all.css'/>");
		buffer.append("</head>");

		body.invoke(writer);

		buffer.append("</div>");

		out.println(writer);
	}

}
