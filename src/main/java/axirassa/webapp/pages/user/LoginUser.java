
package axirassa.webapp.pages.user;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Secure;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.tynamo.security.services.SecurityService;

import axirassa.domain.exception.NoSaltException;
import axirassa.webapp.components.AxForm;

@Secure
public class LoginUser {
	@Inject
	private Session session;

	@Inject
	private SecurityService security;

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

		Subject subject = security.getSubject();
		try {
			subject.login(new UsernamePasswordToken(email, password));
		} catch (AuthenticationException e) {
			form.recordError(e.getLocalizedMessage());
		}
	}


	private void showInvalidLoginMessage() {
		form.recordError("E-mail, password combination was not found in records");
	}


	String onSuccess() {
		return "Index";
	}
}
