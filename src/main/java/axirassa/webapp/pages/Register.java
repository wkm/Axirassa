
package axirassa.webapp.pages;

import org.apache.tapestry5.annotations.Property;

public class Register {
	@Property
	private String email;

	@Property
	private String confirmemail;

	@Property
	private String password;

	@Property
	private String confirmpassword;
}
