
package test.axirassa.webapp.pages;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.tapestry5.dom.Document;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.dom.Node;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.jaxen.JaxenException;
import org.junit.Test;

import axirassa.util.test.MapUtility;
import axirassa.util.test.TapestryPageTest;

import com.formos.tapestry.xpath.TapestryXPath;

public class TestRegisterPage extends TapestryPageTest {
	@Inject
	Session session;


	@Test
	public void registerPage() throws JaxenException {
		System.out.println("TESTER: " + tester);
		Document page = tester.renderPage("user/register");

		List<Node> textNodes = TapestryXPath.xpath("//input[@type='text']").selectNodes(page);
		assertEquals(3, textNodes.size());

		List<Node> passwordNodes = TapestryXPath.xpath("//input[@type='password']").selectNodes(page);
		assertEquals(2, passwordNodes.size());

		Element registerButton = TapestryXPath.xpath("//input[@value='Register']").selectSingleElement(page);
		Document resultPage = tester.clickSubmit(registerButton,
		                                         new MapUtility().p("txtfield", "who@foo.com")
		                                                 .p("txtfield_0", "who@foo.com").p("txtfield_1", "123")
		                                                 .p("txtfield_2", "123").getValueMap());

		// ensure we have no errors
		List<Element> errorNodes = TapestryXPath.xpath("//*[@class='t-error']").selectElements(resultPage);
		assertEquals(0, errorNodes.size());

		System.out.println(resultPage);
	}
}
