
package test.axirassa.services.email;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import axirassa.services.email.EmailTemplate;
import axirassa.services.email.EmailTemplateFactory;
import axirassa.services.email.EmailTemplateType;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class TestEmailTemplateFactory {

	@Test
	public void testFactory() throws IOException, TemplateException {
		Template template = EmailTemplateFactory.instance.getTemplate(EmailTemplate.USER_VERIFY_ACCOUNT,
		                                                              EmailTemplateType.HTML);
		Writer out = new OutputStreamWriter(System.out);

		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put("recipient", "wmacura@gmail.com");
		attributes.put("axlink", "http://localhost/test/asdasda");

		template.process(attributes, out);
		out.flush();
	}
}
