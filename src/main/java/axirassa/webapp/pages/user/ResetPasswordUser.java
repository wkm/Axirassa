
package axirassa.webapp.pages.user;

import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Secure;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;

import axirassa.model.PasswordResetTokenEntity;
import axirassa.model.UserEntity;
import axirassa.webapp.components.AxForm;

@Secure
@RequiresGuest
public class ResetPasswordUser {

	@Inject
	private Session session;

	@Property
	@Persist
	private String email;

	@Component
	private AxForm form;


	void onValidateFromForm() {
		if (email == null) {
			showInvalidEmailMessage();
			return;
		}

		UserEntity entity = UserEntity.getUserByEmail(session, email);
		if (entity == null)
			showInvalidEmailMessage();
	}


	private void showInvalidEmailMessage() {
		form.recordError("No user associated with that e-mail.");
	}


	String onSuccess() {
		PasswordResetTokenEntity token = new PasswordResetTokenEntity();
		token.setPinger(pinger)
	}
}
