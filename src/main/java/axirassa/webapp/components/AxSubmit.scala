
package axirassa.webapp.components

import org.apache.tapestry5.BindingConstants
import org.apache.tapestry5.annotations.Component
import org.apache.tapestry5.annotations.Parameter
import org.apache.tapestry5.corelib.components.Submit

class AxSubmit {
    @Component(publishParameters = "context,defer,disabled,event,image")
    var submit : Submit = _

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    var label : String = _
    def getLabel = label
}
