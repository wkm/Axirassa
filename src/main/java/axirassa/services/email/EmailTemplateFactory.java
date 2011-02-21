
package axirassa.services.email;

import org.antlr.stringtemplate.StringTemplate;

import axirassa.services.util.TemplateFactory;

public class EmailTemplateFactory extends TemplateFactory<EmailTemplate, EmailTemplateType> {

	public static final EmailTemplateFactory instance = new EmailTemplateFactory();

	static {
		instance.buildInstances(EmailTemplate.values(), EmailTemplateType.values());
	}


	public static StringTemplate getTemplateInstance(EmailTemplate template, EmailTemplateType type) {
		return instance.getInstance(template, type);
	}
}
