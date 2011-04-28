
package axirassa.services.pinger

import axirassa.trigger.Trigger
import java.io.IOException
import java.net.SocketException
import java.net.UnknownHostException
import java.util.Collection
import java.util.Date
import java.util.HashMap
import java.util.LinkedHashMap

import org.apache.http.HttpResponse
import org.apache.http.NoHttpResponseException
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.methods.HttpGet
import org.apache.http.conn.HttpHostConnectException

import axirassa.model.HttpStatisticsEntity
import axirassa.model.PingerEntity

object HttpPinger {
    val USER_AGENT = "Mozilla/5.0 (axirassa-pinger 0.5 en-us)"
}

class HttpPinger(client : InstrumentedHttpClient = new InstrumentedHttpClient) {

    var triggers = new HashMap[Class[_ <: Trigger], Trigger]()

    def resetTriggers() {
        triggers = new HashMap
    }

    def addTrigger(trigger : Trigger) {
        triggers.put(trigger.getClass(), trigger)
    }

    def getTriggers = triggers.values
    def getTrigger[T](classObject : Class[_ <: T]) = triggers.get(classObject).asInstanceOf[T]

    def ping(entity : PingerEntity) = {
        resetTriggers()

        val get = new HttpGet(entity.getUrl())
        get.setHeader("User-Agent", HttpPinger.USER_AGENT)

        var statistic : HttpStatisticsEntity = null

        try {
            val response = client.executeWithInstrumentation(get)

            statistic = new HttpStatisticsEntity()
            statistic.setTimestamp(new Date())
            statistic.setPinger(entity)
            statistic.setLatency(client.getLatency())
            statistic.setResponseTime(client.getResponseTime)
            statistic.setStatusCode(response.getStatusLine.getStatusCode())
            statistic.setResponseSize(client.getResponseContent.length())
            statistic.setUncompressedSize(0)

            System.out.printf("Latency: %dms Response: %dms Status: [%d] URL: %s\n", statistic.getLatency,
                statistic.getResponseTime, statistic.getStatusCode, statistic.getPinger.getUrl)

            addTrigger(new StatusCodeTrigger(statistic.getStatusCode()))
        } catch {
            case e : HttpHostConnectException => addTrigger(new CouldNotConnectTrigger(e))
            case e : NoHttpResponseException => addTrigger(new NoResponseTrigger(e))
            case e : SocketException => addTrigger(new ConnectionTimeOutTrigger(e))
            case e : UnknownHostException => addTrigger(new UnknownHostTrigger())
            case e : ClientProtocolException => addTrigger(new ProtocolErrorTrigger(e))
            case e : Exception => {
                e.printStackTrace(System.err)
                addTrigger(new PingerErrorTrigger(e))
            }
        }

        statistic
    }
}
