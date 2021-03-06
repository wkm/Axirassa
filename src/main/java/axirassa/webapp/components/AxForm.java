
package axirassa.webapp.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Field;
import org.apache.tapestry5.FormValidationControl;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ValidationDecorator;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Environment;

import axirassa.webapp.services.validation.CustomValidationDecorator;

@Import(library = { "context:js/error.js" }, stylesheet = { "context:/css/form.css" })
public class AxForm implements FormValidationControl {

	@Inject
	private Environment environment;

	@Component(publishParameters = "autofocus")
	private Form form;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String title;


	public String getTitle() {
		return title;
	}


	@Parameter
	private boolean hasTitle;


	public boolean hasTitle() {
		if (title != null)
			return true;
		else
			return false;
	}


	void beginRender(MarkupWriter writer) {
		environment.push(ValidationDecorator.class, new CustomValidationDecorator(environment, writer));
	}


	void afterRender(MarkupWriter writer) {
		environment.pop(ValidationDecorator.class);
	}


	@Override
	public void recordError(String errorMessage) {
		form.recordError(errorMessage);

	}


	@Override
	public void recordError(Field field, String errorMessage) {
		form.recordError(field, errorMessage);
	}


	@Override
	public boolean getHasErrors() {
		return form.getHasErrors();
	}


	@Override
	public boolean isValid() {
		return form.isValid();
	}


	@Override
	public void clearErrors() {
		form.clearErrors();
	}

}
