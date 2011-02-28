
package axirassa.webapp.pages.user;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Secure;

import axirassa.webapp.components.AxCheckbox;
import axirassa.webapp.components.AxForm;

@Secure
@RequiresUser
public class AddPhoneNumberUser {
	@Property
	private String phoneNumber;

	@Property
	private String extension;

	@Property
	private boolean acceptsVoice;

	@Property
	private boolean acceptsText;

	@Component
	private AxCheckbox acceptsTextField;

	@Component
	private AxCheckbox acceptsVoiceField;

	@Component
	private AxForm form;


	public void onValidateFromForm() {
		if (extension != null && acceptsText == true)
			form.recordError(acceptsTextField, "Text messages may not be sent to phone numbers with extensions");

		if (!acceptsText && !acceptsVoice)
			form.recordError("Please specify your notification method preference");
	}
}
