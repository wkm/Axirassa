
package axirassa.webapp.ajax.httpstream

import java.io.PrintWriter

import javax.servlet.http.HttpServletResponse

/**
 * A utility class for writing JSON onto a streaming HTTP servlet print writer.
 * The class will place repeated calls to {@link #write(String)} into a JSON
 * array.
 *
 * When results are streamed they're wrapped with "#START#" and "#END#". It is
 * vital that the presence of these delimiters is verified, when a response is
 * streamed it's possible multiple batches will get grouped together enroute or
 * a single batch may be split up.
 *
 * @author wiktor
 */
class JSONStreamPrintWriter(writer : PrintWriter) {
  private var messageCount = 0

  /**
   * writes this string as raw JSON onto the current batch of JSON being
   * streamed
   */
  def write(json : String) {
    if (messageCount == 0)
      writer.append("<<#START#>>[")
    else
      writer.append(',')

    writer.write(json)

    messageCount += 1
  }

  /**
   * sends this segment of JSON down the pipe note you'll also need to flush
   * the response buffers with {@link HttpServletResponse#flushBuffer()}.
   */
  def flush() {
    if (messageCount > 0) {
      writer.append("]<<#END#>>")

      messageCount = 0

      writer.flush()
    }
  }

  /**
   * calls {@link #flush()} and then closes the print writer
   */
  def close() {
    flush()
    writer.close()
  }
}
