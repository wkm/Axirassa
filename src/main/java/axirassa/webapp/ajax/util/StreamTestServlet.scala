
package axirassa.webapp.ajax.util

import java.util.Date

import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class StreamTestServlet extends HttpServlet {
  override def doGet(request : HttpServletRequest, response : HttpServletResponse) {
    response.setContentType("application/json")

    val writer = response.getWriter()

    for (i <- 0 until 5) {
      writer.printf("\"%s\"\n", new Date().toString())
      writer.flush()
      response.flushBuffer()

      Thread.sleep(1000)
    }

    writer.close()
  }
}
