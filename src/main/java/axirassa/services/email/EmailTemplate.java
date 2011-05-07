
package axirassa.services.email;

import lombok.Getter;
import axirassa.services.util.TemplateEnumeration;

public enum EmailTemplate implements TemplateEnumeration {

	USER_VERIFY_ACCOUNT("account/VerifyAccountEmail", EmailSenderAddress.ACCOUNT),
	USER_RESET_PASSWORD("account/ResetPasswordEmail", EmailSenderAddress.ACCOUNT),
	USER_CHANGE_PASSWORD("account/PasswordChangeEmail", EmailSenderAddress.ACCOUNT),
	USER_PRIMARY_EMAIL_CHANGED("account/PrimaryEmailChangedEmail", EmailSenderAddress.ACCOUNT),
	AGGREGATED_FEEDBACK("support/AggregatedFeedbackEmail", EmailSenderAddress.INTERNAL),
	USER_ADD_EMAIL("support/EmailAddedEmail", EmailSenderAddress.ACCOUNT);

	public static final String BASE_LOCATION = "/axirassa/webapp/emails/";

	@Getter
	private final String location;

	@Getter
	private final String fromAddress;


	EmailTemplate(String location, EmailSenderAddress fromAddress) {
		this.location = location;
		this.fromAddress = fromAddress.getAddress();
	}


	@Override
	public String getFullLocation() {
		return BASE_LOCATION + location;
	}
}
