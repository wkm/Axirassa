
package axirassa.webapp.pages.user;

import java.util.List;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Secure;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Query;
import org.hibernate.Session;

import axirassa.domain.UserModel;
import axirassa.domain.exception.NoSaltException;
import axirassa.webapp.components.AxForm;

@Secure
public class LoginUser {
	@Inject
	private Session session;

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

		Query query = session.createQuery("from UserModel where email=:email");
		query.setString("email", email);

		List<UserModel> users = query.list();

		if (users.size() < 0) {
			showInvalidLoginMessage();
			return;
		}

		UserModel first = users.iterator().next();
		if (!first.matchPassword(password)) {
			showInvalidLoginMessage();
			return;
		}

		// LOGIN
	}


	private void showInvalidLoginMessage() {
		form.recordError("E-mail, password combination was not found in records");
	}


	String onSuccess() {
		return "Index";
	}
}
