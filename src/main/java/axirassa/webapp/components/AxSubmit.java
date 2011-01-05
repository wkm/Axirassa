
package axirassa.webapp.components;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.components.Submit;

public class AxSubmit {
	@Component(publishParameters = "context,defer,disabled,event,image")
	private Submit submit;

	@Parameter(defaultPrefix = "literal")
	private String label;


	public String getLabel() {
		return label;
	}
}
