
package axirassa.webapp.components;

import org.apache.tapestry5.annotations.Parameter;

public class AxPlotGroupControl {
	@Parameter(required = true, defaultPrefix = "literal")
	private String label;


	public String getLabel() {
		return label;
	}


	@Parameter(defaultPrefix = "literal")
	private String value;


	public String getValue() {
		if (value == null)
			return label;
		return value;
	}

}
