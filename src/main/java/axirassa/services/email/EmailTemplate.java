
package axirassa.services.email;

import axirassa.services.util.TemplateEnumeration;

public enum EmailTemplate implements TemplateEnumeration {

	USER_VERIFY_ACCOUNT("account/VerifyAccountEmail", "account@axirassa.com"),
	USER_RESET_PASSWORD("account/ResetPasswordEmail", "account@axirassa.com");

	public static final String BASE_LOCATION = "/axirassa/webapp/emails/";

	private final String location;
	private final String fromAddress;


	EmailTemplate(String location, String fromAddress) {
		this.location = location;
		this.fromAddress = fromAddress;
	}


	@Override
	public String getLocation() {
		return location;
	}


	public String getFromAddress() {
		return fromAddress;
	}


	@Override
	public String getFullLocation() {
		return BASE_LOCATION + location;
	}
}
