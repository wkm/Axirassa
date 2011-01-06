
package axirassa.webapp.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

public class AxFormLabel {
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	@Property
	private String label;
}
