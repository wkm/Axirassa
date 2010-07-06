
package com.zanoccio.axirassa.webapp.tags;

import java.io.IOException;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.zanoccio.axirassa.webapp.tags.exception.ExceptionInTagError;

public class HeaderTag extends SimpleTagSupport implements WebappTag {
	@Override
	public void doTag() {
		JspContext pageContext = getJspContext();
		JspWriter out = pageContext.getOut();

		try {
			out.println("Hello World");
		} catch (IOException e) {
			System.err.println(new ExceptionInTagError(this, e));
		}
	}
}
