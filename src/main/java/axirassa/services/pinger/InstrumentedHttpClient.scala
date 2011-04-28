
package axirassa.services.pinger

import axirassa.services.AxirassaServiceException
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

import org.apache.http.HttpEntity
import org.apache.http.HttpException
import org.apache.http.HttpRequest
import org.apache.http.HttpRequestInterceptor
import org.apache.http.HttpResponse
import org.apache.http.HttpResponseInterceptor
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.protocol.HttpContext

class ExecutedWithoutInstrumentationException(client: InstrumentedHttpClient) 
	extends AxirassaServiceException("InstrumentedHttpClient executed without instrumentation")

object InstrumentedHttpClient {
	val NANOS_PER_MILLI = 1000000
	
	def readInputStreamBuffer(input : InputStream ) = {
		val reader = new InputStreamReader(input)
		val sb = new StringBuilder(input.available())

		val buffer = new Array[Char](4096)
		var length = 0
		
		while ((length = reader.read(buffer)) != -1)
			sb.append(buffer, 0, length)

		reader.close()
		input.close()

		sb.toString()
	}
}

class InstrumentedHttpClient extends DefaultHttpClient {
	var startTick : Long = 0
	var latencyTick : Long = 0
	var responseTick : Long =0
	
	var isInstrumented = false
	var isStatisticInvalid = false
	
	var responseContent : String = _

	addRequestInterceptor(new HttpRequestInterceptor() {
		override
		def process(request : HttpRequest, context : HttpContext) {
			if (!isInstrumented)
				isStatisticInvalid = true

			if (startTick == 0)
				startTick = System.nanoTime()
		}
	})

	addResponseInterceptor(new HttpResponseInterceptor() {
		override
		def process(response : HttpResponse, context :HttpContext) {
			if (!isInstrumented)
				isStatisticInvalid = true

			if (latencyTick == 0)
				latencyTick = System.nanoTime()
		}
	})
	
	


	private def resetTimings {
		startTick = 0
		latencyTick = 0
		responseTick = 0
	}


	def executeWithInstrumentation(request : HttpUriRequest ) {
		try {
			isInstrumented = true
			isStatisticInvalid = false
			resetTimings

			val response = execute(request)
			val entity = response.getEntity()

			if (entity != null) {
				responseContent = InstrumentedHttpClient.readInputStreamBuffer(entity.getContent())
			}

			responseTick = System.nanoTime()

			response
		} finally
			isInstrumented = false
	}


	/**
	 * @return total time to receive the complete response, in milliseconds
	 */
	def getResponseTime = {
		if (isStatisticInvalid)
			throw new ExecutedWithoutInstrumentationException(this)

	((responseTick - startTick) / InstrumentedHttpClient.NANOS_PER_MILLI).asInstanceOf[Int]
	}


	/**
	 * @return time from the sending of the request to the first byte of the
	 *         response, in milliseconds
	 */
	def getLatency() = {
		if (isStatisticInvalid)
			throw new ExecutedWithoutInstrumentationException(this)

		 ((latencyTick - startTick) / InstrumentedHttpClient.NANOS_PER_MILLI).asInstanceOf[Int]
	}


	def getResponseContent = responseContent

}
