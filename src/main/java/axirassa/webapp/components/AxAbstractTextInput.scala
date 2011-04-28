
package axirassa.webapp.components

import org.apache.tapestry5.BindingConstants
import org.apache.tapestry5.ComponentResources
import org.apache.tapestry5.Field
import org.apache.tapestry5.annotations.Parameter
import org.apache.tapestry5.annotations.Property
import org.apache.tapestry5.ioc.annotations.Inject

abstract class AxAbstractTextInput extends Field() {

    @Inject
    var resources : ComponentResources = _

    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    var name : String = _
    def getName = name

    @Property
    var text : String = _

    @Parameter
    var label : String = _

    def setLabel(label0 : String) {
        label = label0
    }

    override def getLabel() =
        if (label == null)
            resources.getId()
        else
            label
}
