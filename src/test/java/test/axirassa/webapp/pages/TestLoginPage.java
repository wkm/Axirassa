
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

public class TestLoginPage extends TapestryPageTest {
	@Inject
	CreateUserFlow createUser;

	@Inject
	Session session;


	@Test
	public void structure() throws JaxenException {
		Document page = tester.renderPage("user/login");

		ensureHasElementById(page, "emailField");
		ensureHasElementById(page, "passwordField");

		assertEquals("E-mail", getLabelTextFor(page, "emailField"));
		assertEquals("Password", getLabelTextFor(page, "passwordField"));
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

		// System.out.println(result);
		ensureNoErrors(result);
	}
}
