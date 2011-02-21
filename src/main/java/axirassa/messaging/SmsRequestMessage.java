
package axirassa.messaging;

import java.util.HashMap;

import axirassa.util.AutoSerializingObject;

public class SmsRequestMessage extends AutoSerializingObject {
	private static final long serialVersionUID = 6558222308712420947L;

	private final PhoneMessageTemplate template;
	private String phoneNumber;
	private final HashMap<String, Object> attributeMap = new HashMap<String, Object>();
}
