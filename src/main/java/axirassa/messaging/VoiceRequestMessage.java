
package axirassa.messaging;

import axirassa.services.phone.PhoneTemplate;
import axirassa.services.util.TemplateFillingMessage;

public class VoiceRequestMessage extends TemplateFillingMessage {
	private static final long serialVersionUID = 5054354656690374716L;

	private final PhoneTemplate template;
	private final String phoneNumber;
	private final String extension;


	public VoiceRequestMessage(String phoneNumber, String extension, PhoneTemplate template) {
		this.phoneNumber = phoneNumber;
		this.extension = extension;
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
