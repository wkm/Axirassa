
package axirassa.webapp.components

import org.apache.tapestry5.BindingConstants
import org.apache.tapestry5.Field
import org.apache.tapestry5.MarkupWriter
import org.apache.tapestry5.annotations.Environmental
import org.apache.tapestry5.annotations.Parameter
import org.apache.tapestry5.dom.Element
import org.apache.tapestry5.services.Heartbeat

class AxError {
	@Parameter(name = "for", required = true, allowNull = false, defaultPrefix = BindingConstants.COMPONENT)
	var field : Field  = _

	@Parameter
	var value : String = _

	@Environmental
	var heartbeat : Heartbeat = _

	var labelElement : Element = _


	def beginRender(writer : MarkupWriter ) {
		val field = this.field
		labelElement = writer.element("span", "class", "msg")

		val command = new Runnable() {
			@Override
			def run() {
				val fieldId = field.getClientId()
				labelElement.forceAttributes("id", fieldId + "-msg")
			}
		}

		heartbeat.defer(command)
	}


	def afterRender(writer : MarkupWriter ) {
		writer.write(value)
		writer.end()
	}
}
