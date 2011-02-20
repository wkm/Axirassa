
package axirassa.services.email;

import java.io.Serializable;

public enum EmailTemplate implements Serializable {

	USER_VERIFY_ACCOUNT("account/VerifyAccountEmail", "account@axirassa.com"),
	USER_RESET_PASSWORD("account/ResetPasswordEmail", "account@axirassa.com");

	public static final String BASE_LOCATION = "/axirassa/webapp/emails/";

	private final String location;
	private final String fromAddress;


	EmailTemplate(String location, String fromAddress) {
		this.location = location;
		this.fromAddress = fromAddress;
	}


	public String getLocation() {
		return location;
	}


	public String getFromAddress() {
		return fromAddress;
	}


	public String getFullLocation() {
		return BASE_LOCATION + location;
	}
}
