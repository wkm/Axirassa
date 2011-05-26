
package test.axirassa.webapp.pages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.tapestry5.dom.Document;
import org.apache.tapestry5.dom.Node;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.jaxen.JaxenException;
import org.junit.Test;

import axirassa.dao.UserEmailAddressDAO;
import axirassa.model.UserEntity;
import axirassa.model.flows.CreateUserFlow;
import axirassa.util.test.TapestryPageTest;

import com.formos.tapestry.xpath.TapestryXPath;

public class TestRegisterPage extends TapestryPageTest {
	@Inject
	UserEmailAddressDAO emailDAO;

	@Inject
	CreateUserFlow createUser;


	private Document registerPage() {
		return tester.renderPage("user/register");
	}


	@Test
	public void register() throws Exception {
		Document page = registerPage();

		List<Node> textNodes = TapestryXPath.xpath("//input[@type='text']").selectNodes(page);
		assertEquals(3, textNodes.size()); // an extra node for feedback

		List<Node> passwordNodes = TapestryXPath.xpath("//input[@type='password']").selectNodes(page);
		assertEquals(2, passwordNodes.size());

		Document resultPage = clickSubmitByValue(page, "Register", new LinkedHashMap<String, String>() {
			{
				put("emailField", "who@foo.com");
				put("confirmEmailField", "who@foo.com");
				put("passwordField", "123");
				put("confirmPasswordField", "123");
			}
		});

		ensureNoErrors(resultPage);

		// did we save this user?
		UserEntity user = emailDAO.getUserByEmail("who@foo.com");
		assertNotNull(user);

		// password should match
		assertTrue(user.matchPassword("123"));
	}


	@Test
	public void emptyFields() throws JaxenException {
		Document result = clickSubmitByValue(registerPage(), "Register");

		ensureErrorOnField(result, "emailField");
		ensureErrorOnField(result, "confirmEmailField");
		ensureErrorOnField(result, "passwordField");
		ensureErrorOnField(result, "confirmPasswordField");
	}


	@Test
	public void differentEmailsAndPasswords() throws JaxenException {
		Document result = clickSubmitByValue(registerPage(), "Register", new LinkedHashMap<String, String>() {
			{
				put("emailField", "what@foo.com");
				put("confirmEmailField", "where@foo.com");

				put("passwordField", "123");
				put("confirmPasswordField", "abc");
			}
		});

		ensureNoErrorOnField(result, "emailField");
		ensureNoErrorOnField(result, "passwordField");

		ensureErrorOnField(result, "confirmEmailField");
		ensureErrorOnField(result, "confirmPasswordField");
	}


	@Test
	public void invalidEmail() throws JaxenException {
		Document result = clickSubmitByValue(registerPage(), "Register", new LinkedHashMap<String, String>() {
			{
				put("emailField", "notAnEmail");
			}
		});

		ensureErrorOnField(result, "emailField");
	}
}
