
package axirassa.webapp.pages.user;

import java.util.List;

import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Secure;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Query;
import org.hibernate.Session;

import axirassa.domain.UserModel;
import axirassa.domain.exception.NoSaltException;

@Secure
public class LoginUser {
	@Inject
	private Session session;

	@Persist
	@Property
	private String email;

	@Property
	private String password;


	String onSuccess() throws NoSaltException {
		Query query = session.createQuery("from UserModel where email=:email");
		query.setString("email", email);

		List<UserModel> users = query.list();

		if (users.size() < 0)
			// no user's with that e-mail
			return "Login";

		UserModel first = users.iterator().next();
		if (first.matchPassword(password))
			// LOGIN
			return "Index";
		else
			return "User/Login";

	}
}
