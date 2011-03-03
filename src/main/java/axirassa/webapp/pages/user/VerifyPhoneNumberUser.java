
package axirassa.webapp.pages.user;

import java.io.IOException;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.hornetq.api.core.HornetQException;

import axirassa.model.UserPhoneNumberEntity;
import axirassa.services.phone.PhoneTemplate;
import axirassa.util.RandomStringGenerator;
import axirassa.webapp.components.AxForm;
import axirassa.webapp.components.AxTextField;
import axirassa.webapp.services.AxirassaSecurityService;
import axirassa.webapp.services.SmsNotifyService;
import axirassa.webapp.services.VoiceNotifyService;
import axirassa.webapp.services.exceptions.AxirassaSecurityException;

@RequiresAuthentication
public class VerifyPhoneNumberUser {
	@Inject
	private Session database;

	@Inject
	private AxirassaSecurityService security;

	@Inject
	private VoiceNotifyService voice;

	@Inject
	private SmsNotifyService sms;

	@Property
	@Persist
	private String token;

	@Property
	private UserPhoneNumberEntity phoneNumberEntity;

	@Component
	private AxForm form;

	@Component
	private AxTextField verificationCodeField;

	@Property
	private String verificationCode;

	private Long phoneNumberId;


	public Object onActivate(Long phoneNumberId) throws AxirassaSecurityException {
		this.phoneNumberId = phoneNumberId;
		phoneNumberEntity = UserPhoneNumberEntity.getByIdWithUser(database, phoneNumberId);
		security.verifyOwnership(phoneNumberEntity);

		if (!phoneNumberEntity.getUser().getEmail().equals(security.getEmail()))
			return false;

		return true;
	}


	public Object onPassivate() {
		return phoneNumberId;
	}


	public Object onActionFromSendSMS(Long phoneNumberId) throws AxirassaSecurityException, HornetQException,
	        IOException {
		phoneNumberEntity = UserPhoneNumberEntity.getByIdWithUser(database, phoneNumberId);
		security.verifyOwnership(phoneNumberEntity);

		if (!phoneNumberEntity.isAcceptingSms())
			return false;

		sendCodeBySms(phoneNumberEntity.getFormattedToken());

		return true;
	}


	public Object onActionFromSendVoice(Long phoneNumberId) throws AxirassaSecurityException, HornetQException,
	        IOException {
		phoneNumberEntity = UserPhoneNumberEntity.getByIdWithUser(database, phoneNumberId);
		security.verifyOwnership(phoneNumberEntity);

		if (!phoneNumberEntity.isAcceptingVoice())
			return false;

		sendCodeByVoice(phoneNumberEntity.getFormattedToken());

		return true;
	}


	public void onValidateFromForm() {
		if (!verificationCode.equals(phoneNumberEntity.getToken()))
			form.recordError(verificationCodeField, "Verification code not matched");
	}


	@CommitAfter
	public Object onSuccessFromForm() {
		phoneNumberEntity.setConfirmed(true);
		return SettingsUser.class;
	}


	private void sendCodeByVoice(String token) throws HornetQException, IOException {
		voice.startMessage(PhoneTemplate.USER_VERIFY_PHONE_NUMBER);
		voice.setPhoneNumber(phoneNumberEntity.getPhoneNumber());
		voice.setExtension(phoneNumberEntity.getExtension());
		voice.addAttribute("code", RandomStringGenerator.makeRandomStringToken(8));
		voice.addAttribute("user", security.getEmail());
		voice.send();
	}


	private void sendCodeBySms(String token) throws HornetQException, IOException {
		sms.startMessage(PhoneTemplate.USER_VERIFY_PHONE_NUMBER);
		sms.setPhoneNumber(phoneNumberEntity.getPhoneNumber());
		sms.addAttribute("code", token);
		sms.addAttribute("user", security.getEmail());
		sms.send();
	}
}
