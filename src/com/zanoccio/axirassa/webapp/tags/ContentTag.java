
package com.zanoccio.axirassa.webapp.tags;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.JspFragment;

public class ContentTag extends WebappTag {

	private String title = "";


	public void setTitle(String title) {
		this.title = title;
	}


	@Override
	public void doTagExecute() throws IOException, JspException {
		JspFragment body = getJspBody();
		JspContext context = getJspContext();
		JspWriter out = context.getOut();

		StringWriter writer = new StringWriter();
		StringBuffer buffer = writer.getBuffer();

		buffer.append("<div id='content'>");
		buffer.append("<h1>").append(title).append("</h1>");
		body.invoke(writer);
		buffer.append("</div>");
		buffer.append("<div id='footer'>");
		buffer.append("Copyright 2010 -- Zanoccio LLC. All Rights Reserved");
		buffer.append("</div>");

		out.println(writer);
	}
}
