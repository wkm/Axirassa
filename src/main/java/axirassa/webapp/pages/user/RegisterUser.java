
package axirassa.webapp.pages.user;

import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Log;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Secure;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Request;

import axirassa.dao.UserDAO;
import axirassa.model.flows.CreateUserFlow;
import axirassa.webapp.components.AxForm;
import axirassa.webapp.components.AxPasswordField;
import axirassa.webapp.components.AxTextField;

@Secure
@RequiresGuest
public class RegisterUser {
	@Inject
	private Request request;

	@Inject
	private UserDAO userDAO;

	@Inject
	private CreateUserFlow createUserFlow;

	@Property
	private String email;

	@Property
	private String confirmemail;

	@Property
	private String password;

	@Property
	private String confirmpassword;

	@Component
	private AxTextField emailField;

	@Component
	private AxTextField confirmEmailField;

	@Component
	private AxPasswordField confirmPasswordField;

	@Component
	private AxForm form;


	@Log
	public JSONObject onAJAXValidateFromEmailField() {
		String emailvalue = request.getParameter("param");

		if (userDAO.isEmailRegistered(emailvalue))
			return new JSONObject().put("error", emailTakenMessage(emailvalue));

		return new JSONObject();
	}


	private String emailTakenMessage(String email) {
		return "The email '" + email + "' is taken";
	}


	public void onValidateFromForm() {
		if (password != null && confirmemail != null && !password.equals(confirmpassword))
			form.recordError(confirmPasswordField, "Passwords do not match");

		if (email != null && confirmemail != null && !email.equals(confirmemail))
			form.recordError(confirmEmailField, "E-mails do not match");

		if (email != null && userDAO.isEmailRegistered(email))
			form.recordError(emailField, emailTakenMessage(email));
	}


	public Object onSuccessFromForm() throws Exception {
		createUserFlow.setEmail(email);
		createUserFlow.setPassword(password);
		createUserFlow.execute();

		return LoginUser.class;
	}
}
