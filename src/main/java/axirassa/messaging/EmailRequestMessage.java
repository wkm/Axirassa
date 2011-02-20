
package axirassa.messaging;

import java.io.Serializable;
import java.util.HashMap;

import axirassa.services.email.EmailTemplate;
import axirassa.util.AutoSerializingObject;

public class EmailRequestMessage extends AutoSerializingObject implements Serializable {
	private static final long serialVersionUID = 6734725672623808704L;
	private final EmailTemplate template;
	private String toAddress;
	private final HashMap<String, Object> attributeMap = new HashMap<String, Object>();


	public EmailRequestMessage(EmailTemplate template) {
		this.template = template;
	}


	public EmailTemplate getTemplate() {
		return template;
	}


	public void setToAddress(String to) {
		this.toAddress = to;
	}


	public String getToAddress() {
		return toAddress;
	}


	public void addAttribute(String key, Object value) {
		attributeMap.put(key, value);
	}


	public HashMap<String, Object> getAttibuteMap() {
		return attributeMap;
	}

}
