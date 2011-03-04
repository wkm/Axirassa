
package axirassa.services.email;

import java.io.IOException;

import axirassa.services.util.TemplateFactory;
import freemarker.template.Template;

public class EmailTemplateFactory extends TemplateFactory<EmailTemplate, EmailTemplateType> {

	public EmailTemplateFactory() throws IOException {
		super(EmailTemplate.BASE_LOCATION);
	}


	public static final EmailTemplateFactory instance;
	static {
		try {
			instance = new EmailTemplateFactory();
		} catch (IOException e) {
			throw new ExceptionInInitializerError(e);
		}
	}


	public static Template getTemplateInstance(EmailTemplate template, EmailTemplateType type) throws IOException {
		return instance.getTemplate(template, type);
	}
}
