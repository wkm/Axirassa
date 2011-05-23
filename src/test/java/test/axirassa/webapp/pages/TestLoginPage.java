
package test.axirassa.webapp.pages;

import java.util.LinkedHashMap;

import org.apache.tapestry5.dom.Document;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Test;

import axirassa.model.flows.CreateUserFlow;
import axirassa.util.test.TapestryPageTest;

import com.formos.tapestry.xpath.TapestryXPath;

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
		System.out.println("USER LIST: " + users.list());

		Document page = tester.renderPage("user/login");
		Element registerButton = TapestryXPath.xpath("//input[@value='Login']").selectSingleElement(page);
		Document result = tester.clickSubmit(registerButton, new LinkedHashMap<String, String>() {
			{
				put("txtfield", "who@foo.com");
				put("txtfield_0", "123");
			}
		});

		// System.out.println(result);
		ensureNoErrors(result);
	}
}
