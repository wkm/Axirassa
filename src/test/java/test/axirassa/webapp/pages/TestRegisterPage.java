
package test.axirassa.webapp.pages;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.tapestry5.dom.Document;
import org.apache.tapestry5.dom.Node;
import org.jaxen.JaxenException;
import org.junit.Test;

import axirassa.util.test.TapestryPageTest;

import com.formos.tapestry.xpath.TapestryXPath;

public class TestRegisterPage extends TapestryPageTest {
	@Test
	public void registerPage() throws JaxenException {
		System.out.println("TESTER: " + tester);
		Document page = tester.renderPage("user/register");

		List<Node> textNodes = TapestryXPath.xpath("//input[@type='text']").selectNodes(page);
		assertEquals(3, textNodes.size());

		List<Node> passwordNodes = TapestryXPath.xpath("//input[@type='password']").selectNodes(page);
		assertEquals(2, passwordNodes.size());
	}
}
