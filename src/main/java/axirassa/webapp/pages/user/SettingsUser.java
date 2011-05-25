
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
import org.hornetq.api.core.HornetQException;

import axirassa.dao.UserEmailAddressDAO;
import axirassa.dao.UserPhoneNumberDAO;
import axirassa.model.UserEmailAddressEntity;
import axirassa.model.UserEntity;
import axirassa.model.UserPhoneNumberEntity;
import axirassa.model.exception.NoSaltException;
import axirassa.services.email.EmailTemplate;
import axirassa.webapp.components.AxForm;
import axirassa.webapp.components.AxPasswordField;
import axirassa.webapp.services.AxirassaSecurityService;
import axirassa.webapp.services.EmailNotifyService;
import axirassa.webapp.services.exceptions.AxirassaSecurityException;

@Secure
@RequiresAuthentication
public class SettingsUser {

	@Inject
	private AxirassaSecurityService security;

	@Inject
	private UserPhoneNumberDAO userPhoneNumberDAO;

	@Inject
	private UserEmailAddressDAO userEmailAddressDAO;

	@Inject
	private EmailNotifyService emailNotify;

	@Property
	@Persist
	private UserEntity user;


	public Object onActivate() throws AxirassaSecurityException {
		user = security.getUserEntity();

		phoneNumbers = userPhoneNumberDAO.getPhoneNumbersByUser(user);
		if (phoneNumbers.isEmpty())
			hasPhoneNumbers = false;
		else
			hasPhoneNumbers = true;

		emails = userEmailAddressDAO.getEmailsByUser(user);
		if (emails.size() > 0)
			hasAlternateEmails = true;
		else
			hasAlternateEmails = false;

		return true;
	}


	//
	// E-Mails
	//

	@Property
	private boolean hasAlternateEmails;

	@Property
	private List<UserEmailAddressEntity> emails;

	@Property
	private UserEmailAddressEntity email;

	//
	// Phone Numbers
	//
	@Property
	private boolean hasPhoneNumbers;

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
		emailNotify.setToAddress(userEmailAddressDAO.getPrimaryEmail(user).getEmail());
		emailNotify.send();

		return this;
	}

}
