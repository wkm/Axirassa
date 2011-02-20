
package test.axirassa.services.email;

import org.antlr.stringtemplate.StringTemplate;
import org.junit.Test;

import axirassa.services.email.EmailTemplate;
import axirassa.services.email.EmailTemplateFactory;
import axirassa.services.email.EmailTemplateType;

public class TestEmailTemplates {
	@Test
	public void testVerifyAccount() {
		StringTemplate template = EmailTemplateFactory.getTemplateInstance(EmailTemplate.USER_VERIFY_ACCOUNT,
		                                                                   EmailTemplateType.HTML);

		template.setAttribute("email", "who@foo.com");
		template.setAttribute("axlink", "http://axirassa.com/blahblahblah");

		System.out.println(template.toString());
	}
}
