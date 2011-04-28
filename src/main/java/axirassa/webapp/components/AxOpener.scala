
package axirassa.webapp.components

import org.apache.tapestry5.BindingConstants
import org.apache.tapestry5.annotations.Environmental
import org.apache.tapestry5.annotations.Import
import org.apache.tapestry5.annotations.Parameter
import org.apache.tapestry5.services.javascript.JavaScriptSupport

import axirassa.util.RandomStringGenerator

object AxOpener {
    def generateId = "opener_"+RandomStringGenerator.makeRandomStringToken(5)
}

/**
 * Reusable component indicating a
 *
 * @author wiktor
 *
 */
@Import(library = Array("context:js/axcomponents.js"), stylesheet = Array("context:css/axcomponents.css"))
class AxOpener {
    @Environmental
    var javascriptsupport : JavaScriptSupport = _

    /**
     * The identifier for this opener if none is provided one is automatically
     * generated with {@link AxOpener#generateID()}.
     */
    @Parameter
    var id : String = _

    def getId() = {
        if (id == null)
            id = AxOpener.generateId

        id
    }

    /**
     * The header for the opener that is displayed by the opener arrow.
     */
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    var header : String = _
    def getHeader = header

    /**
     * Additional styles to apply to the entire opener.
     */
    @Parameter(defaultPrefix = "literal")
    var style : String = _

    def getStyle() = {
        if (style == null)
            style = ""
        style
    }

    /**
     * Whether the opener should initially be displayed in the open state ---
     * defaults to false.
     */
    @Parameter
    var open : Boolean = _

    def isOpen() = {
        if (open == null)
            open = false

        open
    }

    def getInitialDisplay() = {
        if (isOpen)
            "default"
        else
            "none"
    }

    def getInitialClass() = {
        if (isOpen)
            "axo_open"
        else
            "axo_closed"
    }
}
