
package axirassa.webapp.pages.user;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

import axirassa.webapp.components.AxForm;

public class RegisterUser {
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
	private AxForm registerForm;


	String onSuccess() {
		System.out.println("Processing form.");
		System.out.println("Email: " + email);
		return "Index";
	}
}
