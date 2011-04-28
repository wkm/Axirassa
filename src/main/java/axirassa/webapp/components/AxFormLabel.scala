
package axirassa.webapp.components

import org.apache.tapestry5.BindingConstants
import org.apache.tapestry5.annotations.Parameter
import org.apache.tapestry5.annotations.Property

class AxFormLabel {
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	@Property
	var label : String = _
}
