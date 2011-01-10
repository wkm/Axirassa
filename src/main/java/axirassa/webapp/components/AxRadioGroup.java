
package axirassa.webapp.components;

import java.util.List;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

import axirassa.webapp.utilities.LabeledObject;

public class AxRadioGroup {
	@Parameter(required = false)
	private String label;


	public String getLabel() {
		if (label == null)
			return "";

		return label;
	}


	@Parameter(required = true, principal = true, autoconnect = true)
	@Property
	private LabeledObject value;

	@Parameter(required = true)
	@Property
	private List<LabeledObject> values;

	@Parameter(required = true, allowNull = false)
	@Property
	private ValueEncoder<? extends Object> encoder;
}
