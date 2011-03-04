
package axirassa.services.phone;

import axirassa.services.util.TemplateEnumeration;

public enum PhoneTemplate implements TemplateEnumeration {
	USER_VERIFY_PHONE_NUMBER("account/VerifyPhoneNumber");

	public static final String BASE_LOCATION = "/axirassa/webapp/messages/";

	private final String location;


	PhoneTemplate(String location) {
		this.location = location;
	}


	@Override
	public String getLocation() {
		return location;
	}


	@Override
	public String getFullLocation() {
		return BASE_LOCATION + location;
	}
}
