
package test.axirassa.webapp.components;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.tapestry5.dom.Document;
import org.apache.tapestry5.dom.Element;
import org.jaxen.JaxenException;
import org.junit.Test;

import axirassa.util.test.TapestryPageTest;
import axirassa.webapp.components.AxPasswordField;

public class TestAxPasswordField extends TapestryPageTest {
	@Test
	public void structureTest() throws JaxenException {
		Document page = renderComponentTestPage(AxPasswordField.class);

		Element password = getElementById(page, "password");
		assertNotNull(password);
		assertNull(password.getAttribute("disabled"));
		assertEquals("password", password.getAttribute("type"));

		Element passwordLabel = getElementById(page, "custom");
		assertNotNull(password);
		assertNull(password.getAttribute("disabled"));
	}


	@Test
	public void validationTest() throws JaxenException {
		Document result = clickSubmitByValue(renderComponentTestPage(AxPasswordField.class), "submit");

		ensureErrorOnField(result, "password");
		ensureErrorOnField(result, "custom");

		assertEquals("You must provide a value for Password.", getErrorTextOnField(result, "password"));
		assertEquals("P, P, Password...!?", getErrorTextOnField(result, "custom"));
	}
}
