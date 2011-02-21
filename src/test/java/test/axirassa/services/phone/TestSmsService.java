
package test.axirassa.services.phone;

import java.io.IOException;

import org.antlr.stringtemplate.StringTemplate;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

import axirassa.services.phone.PhoneTemplate;
import axirassa.services.phone.PhoneTemplateFactory;
import axirassa.services.phone.PhoneTemplateType;
import axirassa.services.phone.SendSMS;

public class TestSmsService {
	@Test
	public void testSms() throws ClientProtocolException, IOException {
		HttpClient httpClient = new DefaultHttpClient();

		StringTemplate template = PhoneTemplateFactory.getTemplateInstance(PhoneTemplate.USER_VERIFY_PHONE_NUMBER,
		                                                                   PhoneTemplateType.SMS);
		template.setAttribute("user", "wmacura@gmail.com");
		template.setAttribute("code", "54, 53, 68, 94");

		SendSMS sms = new SendSMS("2175974201", template.toString());
		sms.send(httpClient);
	}
}
