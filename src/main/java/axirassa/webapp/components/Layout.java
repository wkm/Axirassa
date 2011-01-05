
package axirassa.webapp.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;

@Import(stylesheet = { "context:/css/main.css", "context:/css/form.css" })
public class Layout {
	@Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
	private String pageTitle;


	public String getPageTitle() {
		return pageTitle;
	}


	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String header;


	public String getHeader() {
		return header;
	}
}
