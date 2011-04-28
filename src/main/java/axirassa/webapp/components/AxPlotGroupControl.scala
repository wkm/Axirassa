
package axirassa.webapp.components

import org.apache.tapestry5.BindingConstants
import org.apache.tapestry5.annotations.Parameter

class AxPlotGroupControl {
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    var label : String = _
    def getLabel = label

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    var value : String = _
    def getValue = value
}
