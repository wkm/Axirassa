
package axirassa.webapp.pages.user;

import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Log;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Secure;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Request;
import org.hibernate.Session;

import axirassa.domain.UserModel;
import axirassa.domain.exception.NoSaltException;
import axirassa.webapp.components.AxForm;
import axirassa.webapp.components.AxTextField;

@Secure
@RequiresGuest
public class RegisterUser {
	@Inject
	private Request request;

	@Inject
	private Session session;

	@Persist
	@Property
	private String email;

	@Persist
	@Property
	private String confirmemail;

	@Property
	private String password;

	@Property
	private String confirmpassword;

	@Component
	private AxTextField emailField;

	@Component
	private AxForm form;


	@Log
	public JSONObject onAJAXValidateFromEmailField() {
		String emailvalue = request.getParameter("param");

		if (UserModel.isEmailRegistered(session, emailvalue))
			return new JSONObject().put("error", "The email '" + emailvalue + "' is taken");

		return new JSONObject();
	}


	public String onSuccess() throws NoSaltException {
		UserModel newuser = new UserModel();

		newuser.setEMail(email);
		newuser.createPassword(password);

		// we can ignore confirmemail and confirmpassword because validation
		// will have already required them to be identical.

		session.beginTransaction();
		session.save(newuser);
		session.getTransaction().commit();

		return "Index";
	}
}
