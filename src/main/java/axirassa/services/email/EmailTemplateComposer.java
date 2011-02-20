
package axirassa.services.email;

import java.util.HashMap;
import java.util.Map;

import org.antlr.stringtemplate.StringTemplate;

public class EmailTemplateComposer {
	private final EmailTemplate template;
	private final Map<String, Object> attributes = new HashMap<String, Object>();


	public EmailTemplateComposer(EmailTemplate template) {
		this.template = template;
	}


	public void setAttributes(Map<String, Object> attributes) {
		this.attributes.putAll(attributes);
	}


	public StringTemplate composeSubject() {
		StringTemplate instance = EmailTemplateFactory.getTemplateInstance(template, EmailTemplateType.SUBJECT);
		instance.setAttributes(attributes);
		return instance;
	}


	public StringTemplate composeHtml() {
		StringTemplate instance = EmailTemplateFactory.getTemplateInstance(template, EmailTemplateType.HTML);
		instance.setAttributes(attributes);
		return instance;
	}


	public StringTemplate composeText() {
		StringTemplate instance = EmailTemplateFactory.getTemplateInstance(template, EmailTemplateType.TEXT);
		instance.setAttributes(attributes);
		return instance;
	}


	public EmailTemplate getEmailTemplate() {
		return template;
	}
}
