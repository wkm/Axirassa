
package axirassa.webapp.components

import org.apache.tapestry5.BindingConstants
import org.apache.tapestry5.ComponentResources
import org.apache.tapestry5.annotations.Environmental
import org.apache.tapestry5.annotations.Import
import org.apache.tapestry5.annotations.Parameter
import org.apache.tapestry5.ioc.annotations.Inject
import org.apache.tapestry5.services.javascript.JavaScriptSupport

import axirassa.util.RandomStringGenerator

object AxSimplePlot {
    def generateId =
        "plot_"+RandomStringGenerator.instance.randomString(15)
}

@Import(
    library = Array("context:js/axplot.js", "context:js/lib/canvastext.js", "context:js/lib/canvas2image.js"),
    stylesheet = Array("context:css/axplot.css"))
class AxSimplePlot {
    @Environmental
    var javascriptsupport : JavaScriptSupport = _

    @Inject
    var resources : ComponentResources = _

    def setupRender() {
        javascriptsupport.addScript("AxPlot.plot('%s')", getId)
    }

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    var id : String = _

    def getId() = {
        if (id == null)
            id = AxSimplePlot.generateId

        id
    }

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    var source : String = _

    def getSource() = {
        if (source == null)
            source = resources.createEventLink("getPlotData").toAbsoluteURI()

        source
    }
}
