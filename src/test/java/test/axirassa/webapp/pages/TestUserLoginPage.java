
package test.axirassa.webapp.pages;

import static org.junit.Assert.assertEquals;

import java.util.LinkedHashMap;

import org.apache.tapestry5.dom.Document;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.jaxen.JaxenException;
import org.junit.Test;

import axirassa.model.flows.CreateUserFlow;
import axirassa.util.test.TapestryPageTest;

public class TestUserLoginPage extends TapestryPageTest {
	@Inject
	CreateUserFlow createUser;

	@Inject
	Session session;


	@Test
	public void structure() throws JaxenException {
		Document page = renderPage("user/login");

		ensureNoErrors(page);
		ensureHasElementById(page, "emailField");
		ensureHasElementById(page, "passwordField");
		ensureHasElementById(page, "remembermebox");

		assertEquals("E-mail", getLabelTextFor(page, "emailField"));
		assertEquals("Password", getLabelTextFor(page, "passwordField"));
		assertEquals("Stay logged in?", getLabelTextFor(page, "remembermebox"));
	}


	@Test
	public void validateRequiredFields() throws JaxenException {
		Document result = clickSubmitByValue("user/login", "Login");

		ensureErrorOnField(result, "emailField");
		ensureErrorOnField(result, "passwordField");

		assertEquals("Just who are you? You forgot your e-mail.", getErrorTextOnField(result, "emailField"));
		assertEquals("We keep your account safe by requiring a password.", getErrorTextOnField(result, "passwordField"));
	}


	@Test
	public void validateEmailField() throws JaxenException {
		Document result = clickSubmitByValue("user/login", "Login", new LinkedHashMap<String, String>() {
			{
				put("emailField", "who");
				put("passwordField", "123");
			}
		});

		ensureErrorOnField(result, "emailField");

		assertEquals("This doesn't look like an e-mail...", getErrorTextOnField(result, "emailField"));
	}


	@Test
	public void invalidPassword() throws JaxenException {
		Document result = clickSubmitByValue("user/login", "Login", new LinkedHashMap<String, String>() {
			{
				put("emailField", "who@foo.com");
				put("passwordField", "123");
			}
		});

		ensureErrorOnField(result, "emailField");
		assertEquals("E-mail, password combination was not found in records", getErrorTextOnField(result, "emailField"));
	}


	@Test
	public void testLoginEmailWithPlusses() throws Exception {
		createUser.setEmail("who+123@foo.com");
		createUser.setPassword("123");
		createUser.execute();

		Document result = clickSubmitByValue("user/login", "Login", new LinkedHashMap<String, String>() {
			{
				put("emailField", "who+123@foo.com");
				put("passwordField", "123");
			}
		});

		System.err.println(result);

		ensureNoErrors(result);
		logoutUser();
	}


	@Test
	public void testLogin() throws Exception {
		// create a simple user
		createUser.setEmail("who@foo.com");
		createUser.setPassword("123");
		createUser.execute();

		Document result = clickSubmitByValue("user/login", "Login", new LinkedHashMap<String, String>() {
			{
				put("emailField", "who@foo.com");
				put("passwordField", "123");
			}
		});

		ensureNoErrors(result);
		logoutUser();
	}
}
