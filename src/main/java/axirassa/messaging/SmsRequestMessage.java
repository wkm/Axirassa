
package axirassa.messaging;

import axirassa.services.phone.PhoneTemplate;
import axirassa.services.util.TemplateFillingMessage;

public class SmsRequestMessage extends TemplateFillingMessage {
	private static final long serialVersionUID = 6558222308712420947L;

	private final PhoneTemplate template;
	private final String phoneNumber;


	public SmsRequestMessage(String phoneNumber, PhoneTemplate template) {
		this.phoneNumber = phoneNumber;
		this.template = template;
	}


	public PhoneTemplate getTemplate() {
		return template;
	}


	public String getPhoneNumber() {
		return phoneNumber;
	}
}
