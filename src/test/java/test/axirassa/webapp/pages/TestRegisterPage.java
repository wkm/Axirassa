
package test.axirassa.webapp.pages;

import org.apache.tapestry5.dom.Document;
import org.junit.Test;

import axirassa.util.test.TapestryPageTest;

public class TestRegisterPage extends TapestryPageTest {
	@Test
	public void registerPage() {
		System.out.println("TESTER: " + tester);
		Document page = tester.renderPage("/user/register");
		System.out.println(page);
	}
}
