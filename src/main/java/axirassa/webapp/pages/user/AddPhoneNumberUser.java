
package axirassa.webapp.pages.user;

import java.io.IOException;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Secure;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.hibernate.Session;
import org.hornetq.api.core.HornetQException;
import org.tynamo.security.services.SecurityService;

import axirassa.model.PhoneNumberTokenEntity;
import axirassa.model.UserEntity;
import axirassa.model.UserPhoneNumberEntity;
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
	private SecurityService security;

	@Inject
	private VoiceNotifyService voiceNotify;

	@Inject
	private SmsNotifyService smsNotify;

	@Inject
	private Session session;

	@Inject
	private PageRenderLinkSource linkSource;

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

	private String token;


	public void onValidateFromForm() {
		if (extension != null && acceptsText == true)
			form.recordError(acceptsTextField, "Text messages may not be sent to phone numbers with extensions");

		if (!acceptsText && !acceptsVoice)
			form.recordError("Please specify your notification method preference");
	}


	@CommitAfter
	public Object onSuccess() throws HornetQException, IOException {
		UserEntity user = UserEntity.getUserByEmail(session, (String) security.getSubject().getPrincipal());

		UserPhoneNumberEntity phoneNumberEntity = new UserPhoneNumberEntity();
		phoneNumberEntity.setUser(user);
		phoneNumberEntity.setPhoneNumber(phoneNumber);
		phoneNumberEntity.setExtension(extension);
		phoneNumberEntity.setAcceptingSms(acceptsText);
		phoneNumberEntity.setAcceptingVoice(acceptsVoice);
		phoneNumberEntity.setConfirmed(false);
		session.save(phoneNumberEntity);

		PhoneNumberTokenEntity phoneNumberTokenEntity = new PhoneNumberTokenEntity();
		phoneNumberTokenEntity.setPhoneNumberEntity(phoneNumberEntity);
		session.save(phoneNumberTokenEntity);

		if (acceptsText)
			sendCodeBySms(user.getEMail(), phoneNumberTokenEntity.getToken());
		else
			sendCodeByVoice(user.getEMail(), phoneNumberTokenEntity.getToken());

		Link link = linkSource.createPageRenderLinkWithContext(VerifyPhoneNumberUser.class, phoneNumberEntity);
		return link;
	}


	private void sendCodeByVoice(String userEmail, String token) throws HornetQException, IOException {
		voiceNotify.startMessage(PhoneTemplate.USER_VERIFY_PHONE_NUMBER);
		voiceNotify.setPhoneNumber(phoneNumber);
		voiceNotify.setExtension(extension);
		voiceNotify.addAttribute("code", RandomStringGenerator.makeRandomStringToken(8));
		voiceNotify.addAttribute("user", userEmail);
		voiceNotify.send();
	}


	private void sendCodeBySms(String userEmail, String token) throws HornetQException, IOException {
		smsNotify.startMessage(PhoneTemplate.USER_VERIFY_PHONE_NUMBER);
		smsNotify.setPhoneNumber(phoneNumber);
		smsNotify.addAttribute("code", token);
		smsNotify.addAttribute("user", userEmail);
		smsNotify.send();
	}
}
