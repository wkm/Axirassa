
package axirassa.services.email;

import java.io.Serializable;

public enum EmailTemplate implements Serializable {

	USER_VERIFY_ACCOUNT("account/VerifyAccountEmail"),
	USER_RESET_PASSWORD("account/ResetPasswordEmail");

	public static final String BASE_LOCATION = "/axirassa/webapp/emails/";

	private String location;


	EmailTemplate(String location) {
		this.location = location;
	}


	public String getLocation() {
		return location;
	}


	public String getFullLocation() {
		return BASE_LOCATION + location;
	}
}
