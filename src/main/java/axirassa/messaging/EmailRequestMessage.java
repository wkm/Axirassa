
package axirassa.messaging;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import axirassa.services.email.EmailTemplate;
import axirassa.services.util.TemplateFillingMessage;

public class EmailRequestMessage extends TemplateFillingMessage implements Serializable {
	private static final long serialVersionUID = 6734725672623808704L;
	
	@Getter
	private final EmailTemplate template;
	
	@Setter
	@Getter
	private String toAddress;


	public EmailRequestMessage(EmailTemplate template) {
		this.template = template;
	}
}
