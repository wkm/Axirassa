
package axirassa.services.email;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Template;
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
		Template template = EmailTemplateFactory.getTemplateInstance(this.template, EmailTemplateType.SUBJECT);
		return executeTemplate(template);
	}


	public String composeHtml() throws IOException, TemplateException {
		Template template = EmailTemplateFactory.getTemplateInstance(this.template, EmailTemplateType.HTML);
		return executeTemplate(template);
	}


	public String composeText() throws IOException, TemplateException {
		Template template = EmailTemplateFactory.getTemplateInstance(this.template, EmailTemplateType.TEXT);
		return executeTemplate(template);
	}


	private String executeTemplate(Template template) throws TemplateException, IOException {
		StringWriter writer = new StringWriter();
		template.process(attributes, writer);
		return writer.toString();
	}


	public EmailTemplate getEmailTemplate() {
		return template;
	}
}
