
package test.axirassa.services.email;

import java.io.IOException;

import org.junit.Test;

import axirassa.services.email.EmailTemplate;
import axirassa.services.email.EmailTemplateComposer;
import axirassa.util.test.WithFixtureData;
import freemarker.template.TemplateException;

public class TestEmailTemplateComposer extends WithFixtureData {
	@Test
	public void composeEmail() throws TemplateException, IOException {
		EmailTemplateComposer composer = new EmailTemplateComposer(EmailTemplate.USER_RESET_PASSWORD);

		composer.addAttribute("recipient", "who@foo.com");
		composer.addAttribute("axlink", "http://locallinker.com");

		assertFixtureEquals("resetPasswordSubject", composer.composeSubject());
		assertFixtureEquals("resetPasswordHtml", composer.composeHtml());
		assertFixtureEquals("resetPasswordText", composer.composeText());
	}
}
