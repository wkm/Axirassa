
package axirassa.webapp.pages.user;

import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Secure;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;

import axirassa.dao.UserEmailAddressDAO;
import axirassa.model.UserEntity;
import axirassa.model.flows.ResetPasswordFlow;
import axirassa.webapp.components.AxForm;

@Secure
@RequiresGuest
public class ResetPasswordUser {
	@Inject
	private UserEmailAddressDAO emailDAO;

	@Inject
	private ResetPasswordFlow resetFlow;

	@Property
	private String email;

	@Component
	private AxForm form;


	public void onValidateFromForm() {
		if (email == null) {
			showInvalidEmailMessage();
			return;
		}

		UserEntity entity = emailDAO.getUserByEmail(email);
		if (entity == null)
			showInvalidEmailMessage();
	}


	private void showInvalidEmailMessage() {
		form.recordError("No user associated with that e-mail.");
	}


	@CommitAfter
	public Object onSuccessFromForm() throws Exception {
		UserEntity user = emailDAO.getUserByEmail(email);
		resetFlow.setUserEntity(user);
		resetFlow.execute();

		return PasswordResetSentUser.class;
	}
}
