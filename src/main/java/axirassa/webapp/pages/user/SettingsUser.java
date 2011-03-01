
package axirassa.webapp.pages.user;

import java.io.IOException;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Secure;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.hornetq.api.core.HornetQException;
import org.tynamo.security.services.SecurityService;

import axirassa.model.UserEntity;
import axirassa.model.UserPhoneNumberEntity;
import axirassa.model.exception.NoSaltException;
import axirassa.services.email.EmailTemplate;
import axirassa.webapp.components.AxForm;
import axirassa.webapp.components.AxPasswordField;
import axirassa.webapp.components.AxTextField;
import axirassa.webapp.services.EmailNotifyService;
import axirassa.webapp.services.MessagingSession;

@Secure
@RequiresAuthentication
public class SettingsUser {

	@Inject
	private SecurityService security;

	@Inject
	private Session session;

	@Inject
	private EmailNotifyService emailNotify;

	@Inject
	private MessagingSession messagingSession;

	@Property
	@Persist
	private UserEntity user;


	public Object onActivate() {
		String email = (String) security.getSubject().getPrincipal();
		user = UserEntity.getUserByEmail(session, email);

		primaryEmail = email;

		phoneNumbers = UserPhoneNumberEntity.getPhoneNumbersByUser(session, user);

		return true;
	}


	//
	// Primary E-Mail
	//
	@Property
	private String primaryEmail;

	@Component
	private AxTextField primaryEmailField;

	//
	// Phone Numbers
	//
	@Property
	private List<UserPhoneNumberEntity> phoneNumbers;

	@Property
	private UserPhoneNumberEntity phoneNumber;

	//
	// Password
	//
	@Property
	private String currentPassword;

	@Property
	private String newPassword;

	@Property
	private String confirmPassword;

	@Component
	private AxPasswordField currentPasswordField;

	@Component
	private AxPasswordField confirmPasswordField;

	@Component
	private AxForm passwordForm;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private boolean passwordChanged;


	public void onValidateFromPasswordForm() throws NoSaltException {
		if (currentPassword != null)
			validateCurrentPassword();

		if (newPassword != null && confirmPassword != null && !newPassword.equals(confirmPassword))
			passwordForm.recordError(confirmPasswordField, "Passwords do not match");
	}


	private void validateCurrentPassword() throws NoSaltException {
		if (!user.matchPassword(currentPassword))
			passwordForm.recordError(currentPasswordField, "Incorrect password");
	}


	@CommitAfter
	public Object onSuccessFromPasswordForm() throws IOException, HornetQException {
		user.createPassword(newPassword);
		passwordChanged = true;

		emailNotify.startMessage(EmailTemplate.USER_CHANGE_PASSWORD);
		emailNotify.setToAddress(user.getEMail());
		emailNotify.send();

		return this;
	}

}
