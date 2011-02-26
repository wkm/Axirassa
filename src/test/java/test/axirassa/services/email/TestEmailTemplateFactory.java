
package test.axirassa.services.email;

import org.antlr.stringtemplate.StringTemplate;
import org.junit.Test;

import axirassa.services.email.EmailTemplate;
import axirassa.services.email.EmailTemplateFactory;
import axirassa.services.email.EmailTemplateType;

public class TestEmailTemplateFactory {

	@Test
	public void testFactory() {
		System.out.println(EmailTemplateFactory.instance);

		StringTemplate instance = EmailTemplateFactory.instance.getInstance(EmailTemplate.USER_VERIFY_ACCOUNT,
		                                                                    EmailTemplateType.HTML);
		System.out.println("INSTANCE");
		System.out.println(instance.toString());
	}
}
