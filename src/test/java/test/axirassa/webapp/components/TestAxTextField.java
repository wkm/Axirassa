
package test.axirassa.webapp.components;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.LinkedHashMap;

import org.apache.tapestry5.dom.Document;
import org.apache.tapestry5.dom.Element;
import org.jaxen.JaxenException;
import org.junit.Test;

import axirassa.util.test.TapestryPageTest;
import axirassa.webapp.components.AxTextField;

public class TestAxTextField extends TapestryPageTest {
	@Test
	public void structureTest() throws JaxenException {
		Document page = renderComponentTestPage(AxTextField.class);

		Element txtfield = getElementById(page, "textfield");
		assertNotNull(txtfield);
		assertNull(txtfield.getAttribute("disabled"));

		Element txtfieldLabel = getLabelFor(page, "textfield");
		assertNotNull(txtfieldLabel);
		assertEquals("Textfield", getElementText(txtfieldLabel));
		assertEquals("text", txtfield.getAttribute("type"));

		Element txtfieldDisabled = getElementById(page, "textfieldDisabled");
		assertNotNull(txtfieldDisabled);
		assertNotNull(txtfieldDisabled.getAttribute("disabled"));

		Element txtfieldDisabledLabel = getLabelFor(page, "textfieldDisabled");
		assertNotNull(txtfieldDisabledLabel);
		assertEquals("Disabled", getElementText(txtfieldDisabledLabel));
	}


	@Test
	public void validationTest() throws JaxenException {
		Document page = renderComponentTestPage(AxTextField.class);
		Document result = clickSubmitByValue(page, "submit");

		ensureErrorOnField(result, "textfield");
		assertEquals("You must provide a value for Textfield.", getErrorTextOnField(result, "textfield"));

		ensureErrorOnField(result, "custom");
		assertEquals("You accidently a field", getErrorTextOnField(result, "custom"));

		// disabled field should be fine
		ensureNoErrorOnField(result, "textfieldDisabled");
	}


	@Test
	public void regexpValidation() throws JaxenException {
		Document testPage = renderComponentTestPage(AxTextField.class);
		Document result = clickSubmitByValue(testPage, "submit", new LinkedHashMap<String, String>() {
			{
				put("regexp", "123-45");
			}
		});

		ensureErrorOnField(result, "regexp");
		assertEquals("Invalid format", getErrorTextOnField(result, "regexp"));

		result = clickSubmitByValue(testPage, "submit", new LinkedHashMap<String, String>() {
			{
				put("regexp", "123-45-6789");
			}
		});

		ensureNoErrorOnField(result, "regexp");
	}
}
