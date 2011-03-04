
package axirassa.messaging;

import java.io.Serializable;

import axirassa.services.email.EmailTemplate;
import axirassa.services.util.TemplateFillingMessage;

public class EmailRequestMessage extends TemplateFillingMessage implements Serializable {
	private static final long serialVersionUID = 6734725672623808704L;
	private final EmailTemplate template;
	private String toAddress;


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
}
