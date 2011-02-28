
package axirassa.webapp.pages.user;

import java.io.IOException;

import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Log;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Secure;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Request;
import org.hibernate.Session;
import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientProducer;

import axirassa.config.Messaging;
import axirassa.messaging.EmailRequestMessage;
import axirassa.model.UserEntity;
import axirassa.model.exception.NoSaltException;
import axirassa.services.email.EmailTemplate;
import axirassa.webapp.components.AxForm;
import axirassa.webapp.components.AxPasswordField;
import axirassa.webapp.components.AxTextField;
import axirassa.webapp.services.MessagingSession;

@Secure
@RequiresGuest
public class RegisterUser {
	@Inject
	private Request request;

	@Inject
	private Session session;

	@Inject
	private MessagingSession messagingSession;

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
	private AxTextField confirmEmailField;

	@Component
	private AxPasswordField confirmPasswordField;

	@Component
	private AxForm form;


	@Log
	public JSONObject onAJAXValidateFromEmailField() {
		String emailvalue = request.getParameter("param");

		if (UserEntity.isEmailRegistered(session, emailvalue))
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

		if (UserEntity.isEmailRegistered(session, email))
			form.recordError(emailField, emailTakenMessage(email));
	}


	@CommitAfter
	public Object onSuccessFromForm() throws NoSaltException, HornetQException, IOException {
		EmailRequestMessage request = new EmailRequestMessage(EmailTemplate.USER_VERIFY_ACCOUNT);
		request.setToAddress(email);
		request.addAttribute("axlink", "http://localhost:8080/");

		ClientProducer producer = messagingSession.createProducer(Messaging.NOTIFY_EMAIL_REQUEST);
		ClientMessage message = messagingSession.createMessage(true);
		message.getBodyBuffer().writeBytes(request.toBytes());
		producer.send(message);
		producer.close();

		UserEntity user = new UserEntity();

		user.setEMail(email);
		user.createPassword(password);

		session.persist(user);

		return LoginUser.class;
	}
}
