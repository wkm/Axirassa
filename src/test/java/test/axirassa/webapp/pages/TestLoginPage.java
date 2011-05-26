
package test.axirassa.webapp.pages;

import java.util.LinkedHashMap;

import org.apache.tapestry5.dom.Document;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Test;

import axirassa.model.flows.CreateUserFlow;
import axirassa.util.test.TapestryPageTest;

public class TestLoginPage extends TapestryPageTest {
	@Inject
	CreateUserFlow createUser;

	@Inject
	Session session;


	@Test
	public void testLogin() throws Exception {
		// create a simple user
		createUser.setEmail("who@foo.com");
		createUser.setPassword("123");
		createUser.execute();

		Query users = session.createQuery("from UserEntity");

		Document page = tester.renderPage("user/login");
		Document result = clickSubmitByValue(page, "Login", new LinkedHashMap<String, String>() {
			{
				put("emailField", "who@foo.com");
				put("passwordField", "123");
			}
		});

		// System.out.println(result);
		ensureNoErrors(result);
	}
}
