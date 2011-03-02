
package axirassa.services.email;

import axirassa.services.util.TemplateEnumeration;

public enum EmailTemplate implements TemplateEnumeration {

	USER_VERIFY_ACCOUNT("account/VerifyAccountEmail", EmailSenderAddress.ACCOUNT),
	USER_RESET_PASSWORD("account/ResetPasswordEmail", EmailSenderAddress.ACCOUNT),
	USER_CHANGE_PASSWORD("account/PasswordChangeEmail", EmailSenderAddress.ACCOUNT),
	USER_PRIMARY_EMAIL_CHANGED("account/PrimaryEmailChangedEmail", EmailSenderAddress.ACCOUNT),
	AGGREGATED_FEEDBACK("support/AggregatedFeedbackEmail", EmailSenderAddress.INTERNAL);

	public static final String BASE_LOCATION = "/axirassa/webapp/emails/";

	private final String location;
	private final String fromAddress;


	EmailTemplate(String location, EmailSenderAddress fromAddress) {
		this.location = location;
		this.fromAddress = fromAddress.getAddress();
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
