
package com.zanoccio.axirassa.webapp.components;

import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import com.zanoccio.axirassa.util.RandomStringGenerator;

/**
 * Reusable component indicating a
 * 
 * @author wiktor
 * 
 */
@Import(library = { "context:js/axcomponents.js" }, stylesheet = { "context:css/axcomponents.css" })
public class AxOpener {
	@Environmental
	private JavaScriptSupport javascriptsupport;


	public static String generateID() {
		return "opener_" + RandomStringGenerator.getInstance().randomString(5);
	}


	@Parameter
	private String id;


	public String getID() {
		if (id == null)
			id = generateID();

		return id;
	}


	@Parameter(required = true, defaultPrefix = "literal")
	private String header;


	public String getHeader() {
		return header;
	}


	// STYLE
	@Parameter(defaultPrefix = "literal")
	private String style;


	public String getStyle() {
		if (style == null)
			style = "\"\"";
		return style;
	}
}
