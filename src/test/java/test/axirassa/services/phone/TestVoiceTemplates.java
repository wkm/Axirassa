
package test.axirassa.services.phone;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import axirassa.services.phone.PhoneTemplate;
import axirassa.services.phone.PhoneTemplateFactory;
import axirassa.services.phone.PhoneTemplateType;
import axirassa.util.test.WithFixtureData;
import freemarker.template.TemplateException;

public class TestVoiceTemplates extends WithFixtureData {
	@Test
	public void verifyPhoneNumber() throws TemplateException, IOException {
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("code", "45-68-94-32");
		attributes.put("user", "who@foo.com");

		String text = PhoneTemplateFactory.instance.getText(PhoneTemplate.USER_VERIFY_PHONE_NUMBER,
		                                                    PhoneTemplateType.VOICE, attributes);

		assertFixtureEquals("verifyPhoneNumber", text);
	}
}
