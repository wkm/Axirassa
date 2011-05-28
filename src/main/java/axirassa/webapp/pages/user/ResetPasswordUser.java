
package axirassa.webapp.pages.user;

import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Secure;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;

import axirassa.dao.UserEmailAddressDAO;
import axirassa.model.UserEntity;
import axirassa.model.flows.ResetPasswordFlow;
import axirassa.webapp.components.AxForm;
import axirassa.webapp.components.AxTextField;

@Secure
@RequiresGuest
public class ResetPasswordUser {
	@Inject
	private UserEmailAddressDAO emailDAO;

	@Inject
	private ResetPasswordFlow resetFlow;

	@Property
	private String email;

	@Parameter("message:unknownEmail-message")
	private String unknownEmailMessage;

	@Component
	private AxForm form;

	@Component
	private AxTextField emailField;


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
		form.recordError(emailField, unknownEmailMessage);
	}


	@CommitAfter
	public Object onSuccessFromForm() throws Exception {
		UserEntity user = emailDAO.getUserByEmail(email);
		resetFlow.setUserEntity(user);
		resetFlow.execute();

		return PasswordResetSentUser.class;
	}
}
