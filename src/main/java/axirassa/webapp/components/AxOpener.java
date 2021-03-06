
package axirassa.webapp.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import axirassa.util.RandomStringGenerator;

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
		return "opener_" + RandomStringGenerator.makeRandomStringToken(5);
	}


	/**
	 * The identifier for this opener; if none is provided one is automatically
	 * generated with {@link AxOpener#generateID()}.
	 */
	@Parameter
	private String id;


	public String getId() {
		if (id == null)
			id = generateID();

		return id;
	}


	/**
	 * The header for the opener that is displayed by the opener arrow.
	 */
	@Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
	private String header;


	public String getHeader() {
		return header;
	}


	/**
	 * Additional styles to apply to the entire opener.
	 */
	@Parameter(defaultPrefix = "literal")
	private String style;


	public String getStyle() {
		if (style == null)
			style = "";
		return style;
	}


	/**
	 * Whether the opener should initially be displayed in the open state ---
	 * defaults to false.
	 */
	@Parameter
	private Boolean open;


	public boolean isOpen() {
		if (open == null)
			open = false;

		return open;
	}


	public String getInitialDisplay() {
		if (isOpen())
			return "default";
		else
			return "none";
	}


	public String getInitialClass() {
		if (isOpen())
			return "axo_open";
		else
			return "axo_closed";
	}
}
