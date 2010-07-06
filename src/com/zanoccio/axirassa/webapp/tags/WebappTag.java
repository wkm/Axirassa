
package com.zanoccio.axirassa.webapp.tags;

import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.zanoccio.axirassa.webapp.tags.exception.ExceptionInTagError;

abstract public class WebappTag extends SimpleTagSupport {
	@Override
	public void doTag() {
		try {
			doTagExecute();
		} catch (Exception e) {
			System.err.println(new ExceptionInTagError(this, e));
		}
	}


	abstract public void doTagExecute() throws Exception;
}
