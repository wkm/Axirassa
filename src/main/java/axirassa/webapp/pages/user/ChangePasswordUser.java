
package axirassa.webapp.pages.user;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;

import axirassa.model.PasswordResetTokenEntity;
import axirassa.model.UserEntity;
import axirassa.webapp.components.AxForm;
import axirassa.webapp.components.AxPasswordField;

public class ChangePasswordUser {
	@Inject
	private Session session;

	@Property
	private String token;

	@Property
	private UserEntity user;

	@Property
	private boolean hasToken = false;

	@Property
	private boolean isTokenInvalid = false;

	@Property
	private String currentPassword;

	@Property
	private String newPassword;

	@Property
	private String confirmPassword;

	@Component
	private AxPasswordField confirmPasswordField;

	@Component
	private AxForm form;


	public Object onActivate(String token) {
		if (token == null)
			return true;

		hasToken = true;
		PasswordResetTokenEntity tokenEntity = PasswordResetTokenEntity.getByToken(session, token);

		if (tokenEntity == null) {
			isTokenInvalid = true;
			return true;
		}

		isTokenInvalid = false;
		user = tokenEntity.getUser();

		return true;
	}


	public void onValidate() {
		if (newPassword != null && confirmPassword != null && !newPassword.equals(confirmPassword))
			form.recordError(confirmPasswordField, "Passwords do not match");
	}


	@CommitAfter
	public String onSuccess() {
		if (isTokenInvalid) {
			user.createPassword(newPassword);
		}
	}
}
