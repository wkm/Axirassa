
package axirassa.messaging;

import axirassa.services.phone.PhoneTemplate;
import axirassa.services.util.TemplateFillingMessage;

public class VoiceRequestMessage extends TemplateFillingMessage {
	private static final long serialVersionUID = 5054354656690374716L;

	private final PhoneTemplate template;
	private String phoneNumber;
	private String extension;


	public VoiceRequestMessage(PhoneTemplate template) {
		this.template = template;
	}


	public PhoneTemplate getTemplate() {
		return template;
	}


	public String getPhoneNumber() {
		return phoneNumber;
	}


	public String getExtension() {
		return extension;
	}
}
