
package test.axirassa.webapp.components;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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

		Element txtfield = getElementById(page, "txtfield");
		assertNotNull(txtfield);
		assertNull(txtfield.getAttribute("disabled"));

		Element txtfieldLabel = getLabelFor(page, "txtfield");
		assertNotNull(txtfieldLabel);
		assertEquals("Textfield", getElementText(txtfieldLabel));

		Element txtfieldDisabled = getElementById(page, "txtfield_0");
		assertNotNull(txtfieldDisabled);
		assertNotNull(txtfieldDisabled.getAttribute("disabled"));

		Element txtfieldDisabledLabel = getLabelFor(page, "txtfield_0");
		assertNotNull(txtfieldDisabledLabel);
		assertEquals("Disabled", getElementText(txtfieldDisabledLabel));
	}


	@Test
	public void validationTest() throws JaxenException {
		Document page = renderComponentTestPage(AxTextField.class);
		Document result = clickSubmitByValue(page, "submit");

		ensureErrorOnField(result, "txtfield");
		Element error = getErrorOnField(result, "txtfield");
		assertEquals("You must provide a value for textfield.", getElementText(error));

		// disabled field should be fine
		ensureNoErrorOnField(result, "txtfield_0");
	}
}
