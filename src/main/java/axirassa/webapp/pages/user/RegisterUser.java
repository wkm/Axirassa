
package axirassa.webapp.pages.user;

import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Secure;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;

import axirassa.domain.UserModel;
import axirassa.domain.exception.NoSaltException;

@Secure
public class RegisterUser {
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


	String onSuccess() throws NoSaltException {
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
