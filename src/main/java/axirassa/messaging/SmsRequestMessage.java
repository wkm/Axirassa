
package axirassa.messaging;

import java.util.HashMap;

import axirassa.services.phone.PhoneTemplate;
import axirassa.util.AutoSerializingObject;

public class SmsRequestMessage extends AutoSerializingObject {
	private static final long serialVersionUID = 6558222308712420947L;

	private final PhoneTemplate template;
	private String phoneNumber;
	private final HashMap<String, Object> attributeMap = new HashMap<String, Object>();


	public SmsRequestMessage(PhoneTemplate template) {
		this.template = template;
	}
}
