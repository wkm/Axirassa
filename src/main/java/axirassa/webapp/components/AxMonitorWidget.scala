
package axirassa.webapp.components

import java.io.IOException
import java.util.List

import org.apache.tapestry5.annotations.Import
import org.apache.tapestry5.annotations.Parameter
import org.apache.tapestry5.annotations.Property
import org.apache.tapestry5.ioc.annotations.Inject
import org.apache.tapestry5.json.JSONArray
import org.apache.tapestry5.services.javascript.JavaScriptSupport
import org.hibernate.Session

import axirassa.dao.PingerDAO
import axirassa.model.HttpStatisticsEntity
import axirassa.model.PingerEntity
import axirassa.util.JSONResponse
import axirassa.webapp.services.AxirassaSecurityService
import axirassa.webapp.services.AxirassaSecurityException

@Import(
    stylesheet = Array("context:/css/axwidget.css"),
    library = Array("context:/js/dojo/dojo.js", "context:/js/axwidget.js"))
class AxMonitorWidget {

    @Inject
    var session : Session = _

    @Inject
    var jssupport : JavaScriptSupport = _

    @Inject
    var pingerDAO : PingerDAO = _

    @Inject
    var security : AxirassaSecurityService = _

    @Parameter(required = true, defaultPrefix = "literal")
    @Property
    var pingerId : Long = _

    @Parameter
    var plotId : String = _

    @Property
    var pinger : PingerEntity = _

    @Property
    var pingerName : String = _

    def setupRender() {
        jssupport.addScript("AxPlot(%d)", pingerId.asInstanceOf[Object])

        val pingerRecord = pingerDAO.findPingerById(pingerId)
        if (pingerRecord.isEmpty) {
            System.err.println("UNKNOWN PINGER ID")
            return
        }

        pinger = pingerRecord.get

        // temporary hack to fix
        pingerName = pinger.getUrl().replace("http://", "")

    }

    def onAction(pingerId : Long) = {
        System.err.println("ATTEMPTING TO HANDLE ACTION")

        val pingerRecord = pingerDAO.findPingerById(pingerId)
        if (!pingerRecord.isEmpty) {
            val pinger = pingerRecord.get
            security.verifyOwnership(pinger)

            val dataPoints = pingerDAO.getDataPoints(pinger, 6 * 60 * 60)

            val array = new JSONArray()

            for (datum <- dataPoints) {
                val line = new JSONArray(datum.getTimestampInMillis.asInstanceOf[AnyRef], datum.getResponseTime.asInstanceOf[AnyRef],
                    datum.getResponseSize.asInstanceOf[AnyRef])
                array.put(line)
            }

            new JSONResponse(array)
        } else
            null
    }

    def setPlotId(plotId : String) {
        this.plotId = plotId
    }

    def getPlotId() = pingerId.toString
}
