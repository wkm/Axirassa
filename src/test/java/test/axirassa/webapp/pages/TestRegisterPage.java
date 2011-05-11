
package test.axirassa.webapp.pages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.tapestry5.dom.Document;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.dom.Node;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.junit.Test;

import axirassa.dao.UserDAO;
import axirassa.model.UserEntity;
import axirassa.model.flows.CreateUserFlow;
import axirassa.util.test.MapUtility;
import axirassa.util.test.TapestryPageTest;

import com.formos.tapestry.xpath.TapestryXPath;

public class TestRegisterPage extends TapestryPageTest {
	@Inject
	Session session;

	@Inject
	UserDAO userDAO;

	@Inject
	CreateUserFlow createUser;


	@Test
	public void registerPage() throws Exception {
		Document page = tester.renderPage("user/register");

		List<Node> textNodes = TapestryXPath.xpath("//input[@type='text']").selectNodes(page);
		assertEquals(3, textNodes.size()); // an extra node for feedback

		List<Node> passwordNodes = TapestryXPath.xpath("//input[@type='password']").selectNodes(page);
		assertEquals(2, passwordNodes.size());

		Element registerButton = TapestryXPath.xpath("//input[@value='Register']").selectSingleElement(page);
		Document resultPage = tester.clickSubmit(registerButton,
		                                         new MapUtility().p("txtfield", "who@foo.com")
		                                                 .p("txtfield_0", "who@foo.com").p("txtfield_1", "123")
		                                                 .p("txtfield_2", "123").getValueMap());

		// ensure we have no errors
		List<Element> errorNodes = TapestryXPath.xpath("//*[@class='t-error']").selectElements(resultPage);
		System.out.println(resultPage);
		assertEquals(0, errorNodes.size());

		// did we save this user?
		UserEntity user = userDAO.getUserByEmail("who@foo.com");
		assertNotNull(user);

		// password should match
		assertTrue(user.matchPassword("123"));

		System.out.println(resultPage);
	}
}
