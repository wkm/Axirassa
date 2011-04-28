
package axirassa.services.phone;

import lombok.Getter;
import axirassa.services.util.TemplateEnumeration;

public enum PhoneTemplate implements TemplateEnumeration {
	USER_VERIFY_PHONE_NUMBER("account/VerifyPhoneNumber");

	public static final String BASE_LOCATION = "/axirassa/webapp/messages/";

	@Getter
	private final String location;


	PhoneTemplate (String location) {
		this.location = location;
	}


	@Override
	public String getFullLocation () {
		return BASE_LOCATION + location;
	}
}
