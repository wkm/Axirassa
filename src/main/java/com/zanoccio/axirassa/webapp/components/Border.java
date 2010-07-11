
package com.zanoccio.axirassa.webapp.components;

import org.apache.tapestry5.annotations.Parameter;

public class Border {
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
