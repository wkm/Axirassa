
package com.zanoccio.axirassa.webapp.components;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;

@Import(stylesheet = { "context:/css/main.css", "context:/css/form.css" })
public class Layout {
	@Parameter(required = true, defaultPrefix = "literal")
	private String pageTitle;


	public String getPageTitle() {
		return pageTitle;
	}


	@Parameter(defaultPrefix = "literal")
	private String header;


	public String getHeader() {
		return header;
	}
}
