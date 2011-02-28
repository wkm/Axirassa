
package axirassa.webapp.pages.user;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.subject.Subject;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Secure;
import org.apache.tapestry5.corelib.components.Checkbox;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.tynamo.security.services.SecurityService;

import axirassa.model.exception.NoSaltException;
import axirassa.webapp.components.AxForm;
import axirassa.webapp.pages.MonitorConsole;

@Secure
@RequiresGuest
public class LoginUser {
	@Inject
	private SecurityService security;

	@Property
	private String email;

	@Property
	private String password;

	@Property
	private boolean rememberme;

	@Component
	private Checkbox remembermebox;

	@Component
	private AxForm form;


	public void onValidateFromForm() throws NoSaltException {
		if (email == null || password == null)
			return;

		Subject subject = security.getSubject();
		try {
			UsernamePasswordToken auth = new UsernamePasswordToken(email, password);
			auth.setRememberMe(rememberme);
			subject.login(auth);
		} catch (AuthenticationException e) {
			showInvalidLoginMessage();
		}
	}


	private void showInvalidLoginMessage() {
		form.recordError("E-mail, password combination was not found in records");
	}


	public Object onSuccessFromForm() {
		return MonitorConsole.class;
	}
}
