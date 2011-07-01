
package test.axirassa.webapp.pages;

import static org.junit.Assert.assertEquals;

import java.util.LinkedHashMap;

import org.apache.tapestry5.dom.Document;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.jaxen.JaxenException;
import org.junit.Test;

import axirassa.util.test.TapestryPageTest;
import axirassa.webapp.services.EmailNotifyService;

public class TestUserResetPasswordPage extends TapestryPageTest {
	private static final String PAGE = "user/ResetPassword";
	private static final String SUBMIT = "Send E-mail";

	@Inject
	EmailNotifyService emailNotifier;


	@Test
	public void structure() throws JaxenException {
		Document page = renderPage(PAGE);

		ensureNoErrors(page);
		ensureHasElementById(page, "emailField");
	}


	@Test
	public void noEmail() throws JaxenException {
		Document result = clickSubmitByValue(PAGE, SUBMIT);

		ensureErrorOnField(result, "emailField");
		assertEquals("We need your e-mail to send you the link to reset your password",
		             getErrorTextOnField(result, "emailField"));
	}


	@Test
	public void misformattedEmail() throws JaxenException {
		Document result = clickSubmitByValue(PAGE, SUBMIT, new LinkedHashMap<String, String>() {
			{
				put("emailField", "who");
			}
		});

		ensureErrorOnField(result, "emailField");
		assertEquals("This doesn't look like an e-mail", getErrorTextOnField(result, "emailField"));
	}


	@Test
	public void unknownEmail() throws JaxenException {
		Document result = clickSubmitByValue(PAGE, SUBMIT, new LinkedHashMap<String, String>() {
			{
				put("emailField", "who@foo.com");
			}
		});

		System.err.println(result);

		ensureErrorOnField(result, "emailField");
		assertEquals("We couldn't find any record of this e-mail address", getErrorTextOnField(result, "emailField"));
	}


	@Test
	public void validEmail() throws Exception {
		createUser("who@foo.com", "123");

		Document page = renderPage(PAGE);
	}
}
