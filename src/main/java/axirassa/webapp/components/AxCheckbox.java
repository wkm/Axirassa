
package axirassa.webapp.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.base.AbstractField;
import org.apache.tapestry5.corelib.components.Checkbox;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

public class AxCheckbox extends AbstractField {
	@Inject
	private Request request;

	@Environmental
	private ValidationTracker tracker;

	@Component(publishParameters = "disabled")
	private Checkbox checkboxField;

	@Property
	@Parameter(required = true, principal = true, autoconnect = true)
	private boolean value;

	@Property
	@Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
	private String name;


	@Override
	protected void processSubmission(String elementName) {
		String postedValue = request.getParameter(elementName);
		tracker.recordInput(this, Boolean.toString(postedValue != null));
		value = postedValue != null;
	}

}
