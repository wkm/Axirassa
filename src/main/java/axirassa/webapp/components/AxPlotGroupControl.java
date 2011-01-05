
package axirassa.webapp.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Parameter;

public class AxPlotGroupControl {
	@Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
	private String label;


	public String getLabel() {
		return label;
	}


	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String value;


	public String getValue() {
		if (value == null)
			return label;
		return value;
	}

}
