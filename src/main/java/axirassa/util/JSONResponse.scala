
package axirassa.util

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.UnsupportedEncodingException
import java.util.zip.GZIPOutputStream

import org.apache.tapestry5.StreamResponse
import org.apache.tapestry5.json.JSONCollection
import org.apache.tapestry5.services.Response

/**
 * Based on:
 * http://tapestry.1045711.n5.nabble.com/JSON-GZip-compression-td2845041.html
 * http://tapestry.1045711.n5.nabble.com/T5-2-Tapestry-IoC-Configuration-remove-
 * td2840319.html
 *
 * @author wiktor
 */
object JSONResponse {
  val CHARSET = "UTF-8"
  val MIN_DATA_SIZE = 512
}

class JSONResponse(json : JSONCollection) extends StreamResponse {
  var dataForSending : Array[Byte] = _
  var data = json.toCompactString().getBytes(JSONResponse.CHARSET)
  var isCompressable = false
  if (data.length >= JSONResponse.MIN_DATA_SIZE)
    isCompressable = true

  if (!isCompressable) {
    val dataForSending = data
  }

  compressData()
  data = null

  private def compressData() {
    val outStream = new ByteArrayOutputStream(data.length)
    val gzip = new GZIPOutputStream(outStream)
    gzip.write(data)
    gzip.close()

    dataForSending = outStream.toByteArray()
  }

  override def getContentType = "application/json charset="+JSONResponse.CHARSET

  override def getStream() {
    return new ByteArrayInputStream(dataForSending)
  }

  override def prepareResponse(response : Response) {
    println("PREPARED RESPONSE W/ GZIP LENGTH: "+dataForSending.length)
    if (isCompressable) {
      response.setHeader("Content-Encoding", "gzip")
      response.setIntHeader("Content-Length", dataForSending.length)
    }
  }
}
