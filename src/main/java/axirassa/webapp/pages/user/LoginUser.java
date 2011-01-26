
package axirassa.webapp.pages.user;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Secure;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.tynamo.security.services.SecurityService;

import axirassa.domain.UserModel;
import axirassa.domain.exception.NoSaltException;
import axirassa.webapp.components.AxForm;

@Secure
@RequiresUser
public class LoginUser {
	@Inject
	private Session session;

	@Inject
	private SecurityService securityService;

	@Persist
	@Property
	private String email;

	@Property
	private String password;

	@Component
	private AxForm form;


	void onValidateFromForm() throws NoSaltException {
		if (email == null || password == null) {
			showInvalidLoginMessage();
			return;
		}

		UserModel user = UserModel.getUserByEmail(session, email);

		if (user == null) {
			showInvalidLoginMessage();
			return;
		}

		if (!user.matchPassword(password)) {
			showInvalidLoginMessage();
			return;
		}
	}


	private void showInvalidLoginMessage() {
		form.recordError("E-mail, password combination was not found in records");
	}


	String onSuccess() {
		return "Index";
	}
}
