
package axirassa.webapp.components;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

@Import(
        library = { "context:js/dojo/dojo.js", "context:js/axbuttonbar.js" },
        stylesheet = "context:css/axbuttonbar.css")
public class AxButtonBar {
	@Property
	@Parameter(defaultPrefix = "literal")
	private String name;
}
