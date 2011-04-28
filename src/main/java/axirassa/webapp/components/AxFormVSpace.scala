
package axirassa.webapp.components

import org.apache.tapestry5.annotations.Parameter

class AxFormVSpace {
    @Parameter(required = false, value = "1")
    var height : Int = _
    def getHeight = height
}
