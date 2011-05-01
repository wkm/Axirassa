
package axirassa.webapp.ajax.httpstream

import java.io.IOException
import java.text.ParseException

import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.cometd.bayeux.server.ServerMessage
import org.cometd.server.BayeuxServerImpl
import org.cometd.server.transport.HttpTransport
import org.eclipse.jetty.util.log.Logger

object HttpStreamingTransport {
  val PREFIX = "http-streaming"
}

class HttpStreamingTransport(bayeux : BayeuxServerImpl, name : String) extends HttpTransport(bayeux, name) {
  setOptionPrefix(HttpStreamingTransport.PREFIX)

  info("constructed")

  protected def getLogger() = getBayeux().getLogger()

  def info(args : Any*) {
    val sb = new StringBuffer("#### HTTP STREAMING: ")
    for (arg <- args)
      if (arg == null)
        sb.append("null ")
      else
        sb.append(arg.toString()).append(' ')

    System.err.println(sb.toString())
  }

  def parseRequestMessages(request : HttpServletRequest) =
    parseMessages(request)

  override def getMaxInterval = 120000L

  override def getInterval = 55000L

  override def getMaxLazyTimeout = 100L

  override protected def init() {
    super.init()
    info("initializing")
  }

  override def accept(request : HttpServletRequest) = {
    info("ACCEPTING REQUEST: ", request)
    "POST" == request.getMethod()
  }

  override def handle(request : HttpServletRequest, response : HttpServletResponse) {
    info("HANDLING REQUEST AND RESPONSE")
    val handler = new HttpStreamingTransportHandler(this, request, response)
    handler.handle()
  }

}
