
package axirassa.webapp.services.validation

import org.apache.tapestry5.BaseValidationDecorator
import org.apache.tapestry5.CSSClassConstants
import org.apache.tapestry5.Field
import org.apache.tapestry5.MarkupWriter
import org.apache.tapestry5.ValidationTracker
import org.apache.tapestry5.dom.Element
import org.apache.tapestry5.services.Environment

/**
 * Validations are annoying because there are both client and server side
 * components, each necessarily implemented in Java vs JavaScript.
 * 
 * Based on: http://jumpstart.doublenegative.com.au/jumpstart/examples/input/
 * novalidationbubbles1
 * 
 * @author wiktor
 * 
 */
class CustomValidationDecorator(environment : Environment, markupwriter : MarkupWriter) extends BaseValidationDecorator {
	override
	def insideLabel(field : Field, element : Element) {
		if (field == null)
			return

		if (field.isRequired()) {
			element.addClassName("required-label")
			element.getContainer.addClassName("required-label-c")
		}

		if (inError(field)) {
			element.addClassName("error-label")
			element.getContainer.addClassName("error-label-c")
		}
	}


	override
	def insideField(field : Field) {
		if (field != null && inError(field)) {
			markupwriter.getElement.addClassName(CSSClassConstants.ERROR)
		}
	}


	override
	def afterField( field : Field) {
		if (field.isRequired()) {
			getElement.addClassName("required-field")
			getElement.getContainer.addClassName("required-field-c")
		}

		if (inError(field)) {
			getElement.addClassName("error-field")
			getElement.getContainer.addClassName("error-field-c")
		}
	}

	private def inError(field : Field) = getTracker.inError(field)
	private def getTracker()  = environment.peekRequired(classOf[ValidationTracker])
	private def getElement = markupwriter.getElement
}