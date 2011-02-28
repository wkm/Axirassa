
package axirassa.webapp.pages.user;

import java.io.IOException;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Secure;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hornetq.api.core.HornetQException;

import axirassa.services.phone.PhoneTemplate;
import axirassa.util.RandomStringGenerator;
import axirassa.webapp.components.AxCheckbox;
import axirassa.webapp.components.AxForm;
import axirassa.webapp.services.SmsNotifyService;
import axirassa.webapp.services.VoiceNotifyService;

@Secure
@RequiresUser
public class AddPhoneNumberUser {
	@Inject
	private VoiceNotifyService voiceNotify;

	@Inject
	private SmsNotifyService smsNotify;

	@Property
	@Validate("required,regexp=[-+*#0-9a-zA-Z.\\, ()]+,minlength=10")
	private String phoneNumber;

	@Property
	@Validate("regexp=[-*#0-9]+")
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


	public Object onSuccess() throws HornetQException, IOException {
		smsNotify.startMessage(PhoneTemplate.USER_VERIFY_PHONE_NUMBER);
		smsNotify.setPhoneNumber(phoneNumber);
		smsNotify.addAttribute("code", RandomStringGenerator.makeRandomStringToken(8));
		smsNotify.addAttribute("user", "who@foo.com");
		smsNotify.send();

		return this;
	}
}
