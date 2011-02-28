
package axirassa.webapp.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Checkbox;

public class AxCheckbox {
	@Component(publishParameters = "annotationProvider,clientId,disabled,nulls,translate,validate,value")
	private Checkbox checkboxField;

	@Property
	@Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
	private String label;


	public Checkbox getCheckBox() {
		return checkboxField;
	}


	public String getControlName() {
		return checkboxField.getControlName();
	}


	public boolean isDisabled() {
		return checkboxField.isDisabled();
	}


	public boolean isRequired() {
		return checkboxField.isRequired();
	}


	public String getClientId() {
		return checkboxField.getClientId();
	}
}
