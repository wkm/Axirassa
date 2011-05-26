
package test.axirassa.webapp.pages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.LinkedHashMap;

import org.apache.tapestry5.dom.Document;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.jaxen.JaxenException;
import org.junit.Test;

import axirassa.dao.UserEmailAddressDAO;
import axirassa.model.UserEntity;
import axirassa.model.flows.CreateUserFlow;
import axirassa.util.test.TapestryPageTest;

public class TestRegisterPage extends TapestryPageTest {
	@Inject
	UserEmailAddressDAO emailDAO;

	@Inject
	CreateUserFlow createUser;


	private Document registerPage() {
		return tester.renderPage("user/register");
	}


	@Test
	public void pageStructure() throws JaxenException {
		Document page = registerPage();

		ensureHasElementById(page, "emailField");
		ensureHasElementById(page, "confirmEmailField");
		ensureHasElementById(page, "passwordField");
		ensureHasElementById(page, "confirmPasswordField");

		assertEquals("E-mail", getLabelTextFor(page, "emailField"));
		assertEquals("Confirm e-mail", getLabelTextFor(page, "confirmEmailField"));
		assertEquals("Password", getLabelTextFor(page, "passwordField"));
		assertEquals("Confirm password", getLabelTextFor(page, "confirmPasswordField"));
	}


	@Test
	public void register() throws Exception {
		Document resultPage = clickSubmitByValue(registerPage(), "Register", new LinkedHashMap<String, String>() {
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
