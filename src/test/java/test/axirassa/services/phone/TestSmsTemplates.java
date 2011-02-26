
package test.axirassa.services.phone;

import org.junit.Test;

import axirassa.services.phone.PhoneTemplate;
import axirassa.services.phone.PhoneTemplateFactory;
import axirassa.services.phone.PhoneTemplateType;
import axirassa.util.test.WithFixtureData;
import freemarker.template.Template;

public class TestSmsTemplates extends WithFixtureData {
	@Test
	public void verifyPhoneNumber() {
		Template template = PhoneTemplateFactory.getTemplateInstance(PhoneTemplate.USER_VERIFY_PHONE_NUMBER,
		                                                             PhoneTemplateType.SMS);

		assertFixtureEquals("verifyPhoneNumber", actual);
	}
}
