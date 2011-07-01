
package axirassa.messaging;

import lombok.Getter;
import axirassa.services.phone.PhoneTemplate;
import axirassa.services.util.TemplateFillingMessage;

public class SmsRequestMessage extends TemplateFillingMessage {
	private static final long serialVersionUID = 6558222308712420947L;

	@Getter
	private final PhoneTemplate template;
	
	@Getter
	private final String phoneNumber;


	public SmsRequestMessage(String phoneNumber, PhoneTemplate template) {
		this.phoneNumber = phoneNumber;
		this.template = template;
	}
}
