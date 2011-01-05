
package axirassa.webapp.pages.user;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Secure;

import axirassa.webapp.components.AxForm;

@Secure
public class RegisterUser {
	@Persist
	private String email;


	public void setEmail(String email) {
		this.email = email;
	}


	public String getEmail() {
		return email;
	}


	@Persist
	private String confirmemail;


	public void setConfirmEmail(String email) {
		this.confirmemail = email;
	}


	public String getConfirmEmail() {
		return confirmemail;
	}


	@Property
	private String password;

	@Property
	private String confirmpassword;

	@InjectComponent
	private AxForm registerForm;


	String onSuccess() {
		System.out.println("Processing form.");
		System.out.println("Email: " + email);
		return "Index";
	}
}
