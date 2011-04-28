
package axirassa.webapp.components

import org.apache.tapestry5.BindingConstants
import org.apache.tapestry5.ValidationTracker
import org.apache.tapestry5.annotations.Component
import org.apache.tapestry5.annotations.Environmental
import org.apache.tapestry5.annotations.Parameter
import org.apache.tapestry5.annotations.Property
import org.apache.tapestry5.corelib.base.AbstractField
import org.apache.tapestry5.corelib.components.Checkbox
import org.apache.tapestry5.ioc.annotations.Inject
import org.apache.tapestry5.services.Request

class AxCheckbox extends AbstractField {
    @Inject
    var request : Request = _

    @Environmental
    var tracker : ValidationTracker = _

    @Component(publishParameters = "disabled")
    var checkboxField : Checkbox = _

    @Property
    @Parameter(required = true, principal = true, autoconnect = true)
    var value : Boolean = _

    @Property
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    var name : String = _

    override protected def processSubmission(elementName : String) {
        val postedValue = request.getParameter(elementName)
        tracker.recordInput(this, Boolean.box(postedValue != null).toString)
        value = postedValue != null
    }

}
