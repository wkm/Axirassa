
package axirassa.webapp.components

import org.apache.tapestry5.BindingConstants
import org.apache.tapestry5.ComponentResources
import org.apache.tapestry5.annotations.Environmental
import org.apache.tapestry5.annotations.Import
import org.apache.tapestry5.annotations.Parameter
import org.apache.tapestry5.ioc.annotations.Inject
import org.apache.tapestry5.services.javascript.JavaScriptSupport

import axirassa.util.RandomStringGenerator

object AxPlot {
    def generateID = "plot_"+RandomStringGenerator.getInstance().randomString(5)
}

@Import(library = Array(
    "context:js/flotr.debug-0.2.0-alpha.js", "context:js/sentinel.js", "context:js/lib/canvas2image.js",
    "context:js/lib/canvastext.js"), stylesheet = Array("context:css/axcomponents.css"))
class AxPlot {

    @Environmental
    var javascriptsupport : JavaScriptSupport = _

    @Inject
    var resources : ComponentResources = _

    def setupRender {
        // javascriptsupport.addInitializerCall("plotchart", getID())
        javascriptsupport.addScript("ax.agentcontrol('%s', '%s')", getID, getSource)
    }

    /**
     * An optional unique identifier for this component on the page. If not
     * specified one is randomly generated.
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    var id : String = _

    def getID = {
        if (id == null)
            id = AxPlot.generateID

        id
    }

    /**
     * The data source for this component. By default it's the event handler for
     * the action specified by the tapestry id of this component.
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    var source : String

    def getSource = {
        if (source == null)
            source = resources.createEventLink("action", "4h").toAbsoluteURI()

        source
    }

    // detailsHeader
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    var detailsHeader : String = _

    def getDetailsHeader = {
        if (detailsHeader == null)
            detailsHeader = ""

        detailsHeader
    }

}
