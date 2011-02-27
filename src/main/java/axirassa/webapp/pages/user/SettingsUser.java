
package axirassa.webapp.pages.user;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Secure;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.hornetq.api.core.client.ClientSession;
import org.tynamo.security.services.SecurityService;

import axirassa.model.UserEntity;
import axirassa.webapp.components.AxForm;
import axirassa.webapp.components.AxPasswordField;

@Secure
@RequiresUser
public class SettingsUser {

	@Inject
	private SecurityService security;

	@Inject
	private Session session;

	@Inject
	private ClientSession messagingSession;

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


	public void onValidateFromPasswordForm() {
		if (currentPassword != null)
			validateCurrentPassword();

		if (newPassword != null && confirmPassword != null && !newPassword.equals(confirmPassword))
			passwordForm.recordError(confirmPasswordField, "Passwords do not match");
	}


	private void validateCurrentPassword() {
		String email = (String) security.getSubject().getPrincipal();
		UserEntity user = UserEntity.getUserByEmail(session, email);

	}


	public Object onActionFromPasswordForm() {
		System.out.println("Action from password form");
		return true;
	}

}
