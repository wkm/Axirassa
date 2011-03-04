
package axirassa.services.email;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.TemplateException;

public class EmailTemplateComposer {
	private final EmailTemplate template;
	private final Map<String, Object> attributes = new HashMap<String, Object>();


	public EmailTemplateComposer(EmailTemplate template) {
		this.template = template;
	}


	public void setAttributes(Map<String, Object> attributes) {
		this.attributes.putAll(attributes);
	}


	public void addAttribute(String key, Object value) {
		attributes.put(key, value);
	}


	public String composeSubject() throws TemplateException, IOException {
		return EmailTemplateFactory.instance.getText(template, EmailTemplateType.SUBJECT, attributes);
	}


	public String composeHtml() throws IOException, TemplateException {
		return EmailTemplateFactory.instance.getText(template, EmailTemplateType.HTML, attributes);
	}


	public String composeText() throws IOException, TemplateException {
		return EmailTemplateFactory.instance.getText(template, EmailTemplateType.TEXT, attributes);
	}


	public EmailTemplate getEmailTemplate() {
		return template;
	}
}
