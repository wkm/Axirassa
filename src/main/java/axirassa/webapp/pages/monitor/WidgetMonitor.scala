package axirassa.webapp.pages.monitor

import org.apache.tapestry5.annotations.Property
import axirassa.model.PingerEntity
import axirassa.dao.PingerDAO
import axirassa.webapp.services.AxirassaSecurityService
import org.apache.tapestry5.services.javascript.JavaScriptSupport
import org.apache.tapestry5.ioc.annotations.Inject
import org.apache.tapestry5.annotations.Import
import org.apache.shiro.authz.annotation.RequiresUser
@RequiresUser
@Import(stylesheet = Array("context:/css/axwidget.css", "context:/css/axplot.css"), library = Array(
    "context:/js/dojo/dojo.js", "${tapestry.scriptaculous}/prototype.js", "context:js/flotr.debug-0.2.0-alpha.js",
    "context:/js/lib/canvas2image.js", "context:js/lib/canvastext.js", "context:/js/ajax.js",
    "context:/js/axplot.js", "context:/js/cometd/cometd-namespace.js", "context:/js/cometd/Utils.js",
    "context:/js/cometd/cometd-json.js", "context:/js/cometd/TransportRegistry.js",
    "context:/js/cometd/RequestTransport.js", "context:/js/cometd/Transport.js",
    "context:/js/cometd/LongPollingTransport.js", "context:/js/cometd/CallbackPollingTransport.js",
    "context:/js/cometd/WebSocketTransport.js", "context:/js/cometd/Cometd.js", "context:/js/cometd/dojocometd.js",
    "context:/js/cometd/HttpStreamingTransport.js", "context:/js/cometd/dojox_HttpStreamingTransport.js",
    "context:/js/axwidget.js"))
class WidgetMonitor {
    @Inject
    var jssupport : JavaScriptSupport = _

    def setupRender() {
        jssupport.addScript("axplot = new AxPlot('%s', [[10,20],[20,30],[30,10]])", "plot")
    }

    @Inject
    var security : AxirassaSecurityService = _

    @Inject
    var pingerDAO : PingerDAO = _

    var pinger : PingerEntity = _

    @Property
    var id : Long = _

    @Property
    var pingerName : String = _

    @Property
    var responseTime = 32

    @Property
    var responseSize = 25

    def onActivate(id : Long) = {
        this.id = id

        val entity = pingerDAO.findPingerById(id)
        security.verifyOwnership(entity.get)
        if (entity == null)
            "Index"
        else {
            setPinger(entity.get)
            true
        }
    }

    def beginRender() {
        jssupport.addScript("PingerLevelDataStream.subscribe(%d, null)", id.asInstanceOf[Object])
    }

    def setPinger(pingerEntity : PingerEntity) {
        pinger = pingerEntity
        pingerName = pinger.getUrl().replace("http://", "")
    }

}