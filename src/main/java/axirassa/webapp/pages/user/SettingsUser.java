
package axirassa.webapp.pages.user;

import java.io.IOException;

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
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientProducer;
import org.tynamo.security.services.SecurityService;

import axirassa.config.Messaging;
import axirassa.messaging.EmailRequestMessage;
import axirassa.model.UserEntity;
import axirassa.model.exception.NoSaltException;
import axirassa.services.email.EmailTemplate;
import axirassa.webapp.components.AxForm;
import axirassa.webapp.components.AxPasswordField;
import axirassa.webapp.components.AxTextField;
import axirassa.webapp.services.MessagingSession;

@Secure
@RequiresAuthentication
public class SettingsUser {

	@Inject
	private SecurityService security;

	@Inject
	private Session session;

	@Inject
	private MessagingSession messagingSession;

	//
	// Primary E-Mail
	//
	@Property
	private String primaryEmail;

	@Component
	private AxTextField primaryEmailField;

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
		String email = (String) security.getSubject().getPrincipal();
		UserEntity user = UserEntity.getUserByEmail(session, email);
		if (!user.matchPassword(currentPassword))
			passwordForm.recordError(currentPasswordField, "Incorrect password");
	}


	@CommitAfter
	public Object onSuccessFromPasswordForm() throws IOException, HornetQException {
		String email = (String) security.getSubject().getPrincipal();
		UserEntity user = UserEntity.getUserByEmail(session, email);

		user.createPassword(newPassword);
		passwordChanged = true;

		EmailRequestMessage notification = new EmailRequestMessage(EmailTemplate.USER_CHANGE_PASSWORD);
		notification.setToAddress(user.getEMail());

		ClientProducer producer = messagingSession.createProducer(Messaging.NOTIFY_EMAIL_REQUEST);
		ClientMessage message = messagingSession.createMessage(true);
		message.getBodyBuffer().writeBytes(notification.toBytes());
		producer.send(message);

		return this;
	}

}
