
package test.axirassa.webapp.pages;

import org.apache.tapestry5.dom.Document;
import org.apache.tapestry5.dom.Element;
import org.jaxen.JaxenException;
import org.junit.Test;

import axirassa.util.test.MapUtility;
import axirassa.util.test.TapestryPageTest;

import com.formos.tapestry.xpath.TapestryXPath;

public class TestLoginPage extends TapestryPageTest {
	@Test
	public void testLogin() throws JaxenException {
		Document page = tester.renderPage("user/login");
		Element registerButton = TapestryXPath.xpath("//input[@value='Login']").selectSingleElement(page);
		tester.clickSubmit(registerButton, new MapUtility().p("txtfield", "who@foo.com").p("txtfield_0", "123")
		        .getValueMap());
	}
}
