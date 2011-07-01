
package axirassa.messaging;

import lombok.Getter;
import axirassa.services.phone.PhoneTemplate;
import axirassa.services.util.TemplateFillingMessage;

public class VoiceRequestMessage extends TemplateFillingMessage {
	private static final long serialVersionUID = 5054354656690374716L;

	@Getter
	private final PhoneTemplate template;

	@Getter
	private final String phoneNumber;

	@Getter
	private final String extension;


	public VoiceRequestMessage (String phoneNumber, String extension, PhoneTemplate template) {
		this.phoneNumber = phoneNumber;
		this.extension = extension;
		this.template = template;
	}
}
