
package com.zanoccio.axirassa.webapp.components;

import org.apache.tapestry5.annotations.Parameter;

public class AxInput {
	@Parameter(required = false, defaultPrefix = "literal")
	private String label;


	public String getLabel() {
		return label;
	}

}
