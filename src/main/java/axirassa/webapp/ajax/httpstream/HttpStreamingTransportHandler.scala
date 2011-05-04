
package axirassa.webapp.ajax.httpstream

import java.io.IOException
import java.text.ParseException
import java.lang.Long.toHexString

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.cometd.bayeux.Channel
import org.cometd.bayeux.server.ServerMessage
import org.cometd.server.ServerSessionImpl
import org.eclipse.jetty.continuation.ContinuationSupport

import scala.collection.JavaConversions._

object HttpStreamingTransportHandler {
  val USER_AGENT_HEADER = "User-Agent"
  val SCHEDULER_ATTRIBUTE = "cometd.httpstreaming.scheduler"
  val REQUEST_TICK_ATTRIBUTE = "cometd.httpstreaming.requesttick"
  val JETTY_TIMEOUT_BUFFER = 30 * 1000
}

class HttpStreamingTransportHandler(
  transport : HttpStreamingTransport,
  request : HttpServletRequest,
  response : HttpServletResponse) {

  var serverSession : ServerSessionImpl = _
  var writer : JSONStreamPrintWriter = _
  var requestStartTick = System.nanoTime()

  private def info(args : Any*) {
    val withPrefix = new Array[Any](args.length + 2)
    withPrefix(0) = toHexString(hashCode())
    withPrefix(1) = "   "

    for (i <- 0 until args.length)
      withPrefix(i + 2) = args(i)

    transport.info(withPrefix)
  }

  private def getBayeux() = transport.getBayeux()

  def handle() {
    val schedulerAttribute = request.getAttribute(HttpStreamingTransportHandler.SCHEDULER_ATTRIBUTE)
    info("schdulerAttribute: ", schedulerAttribute)

    val continuation = ContinuationSupport.getContinuation(request)
    // continuation states
    System.err.println("CONTINUATION STATES:")
    if (continuation.isInitial())
      System.err.println("\t >>>> initial")
    if (continuation.isExpired())
      System.err.println("\t >>>> expired")
    if (continuation.isResumed())
      System.err.println("\t >>>> resumed")
    if (continuation.isSuspended())
      System.err.println("\t >>>> suspended")
    System.err.println("\n")

    if (schedulerAttribute == null) {
      info("NO SCHEDULER")
      // the session doesn't have any scheduler associated with it, so it
      // must be new
      handleNewSession()
    } else {
      info("HANDLING RESUME")
      if (!schedulerAttribute.isInstanceOf[HttpStreamingScheduler]) {
        info("DANGER DANGER: scheduler attribute not a HttpStreamingScheduler")
        return
      }

      val tick = request.getAttribute(HttpStreamingTransportHandler.REQUEST_TICK_ATTRIBUTE)
      if (tick == null) {
        info("NO REQUEST TICK")

        // force the session to immediately reconnect
        requestStartTick = 0
        return
      } else {
        if (tick.isInstanceOf[Long])
          requestStartTick = tick.asInstanceOf[Long]
        else {
          info("REQUEST TICK OF WRONG TYPE")
          requestStartTick = 0
        }
      }

      val scheduler = schedulerAttribute.asInstanceOf[HttpStreamingScheduler]
      handleResumedSession(scheduler)
    }
  }

  def handleResumedSession(scheduler : HttpStreamingScheduler) {
    serverSession = scheduler.serverSession

    if (serverSession.isConnected())
      serverSession.startIntervalTimeout()

    sendQueuedMessages()
    suspendSession()
  }

  def handleNewSession() {
    try {
      val messages = transport.parseRequestMessages(request)
      if (messages == null) {
        info("no messages")
        return
      }

      response.setContentType("application/json")

      serverSession = null

      // padInitialReply()

      // process each message
      for (message <- messages)
        serverSession = processMessage(message)

      if (messages.length > 1)
        suspendSession()
    } catch {
      case e : ParseException =>
        info("ERROR PARSING JSON: "+e.getMessage(), e.getCause())
      case e : IOException =>
        e.printStackTrace()
    }
  }

  private def processMessage(message : ServerMessage.Mutable) : ServerSessionImpl = {
    val isConnectMessage = Channel.META_CONNECT.equals(message.getChannel())

    val clientId = message.getClientId()
    if (serverSession == null || isClientSessionMismatch(clientId, serverSession)) {
      serverSession = retrieveSession(clientId)
    } else if (!serverSession.isHandshook()) {
      serverSession = null
    }

    var reply = getBayeux().handle(serverSession, message)
    if (reply != null) {
      var isHandshake = false
      if (serverSession == null) {
        isHandshake = true
        handleHandshake(reply)
      } else {
        info("#### NOT A HANDSHAKE")

        if (isConnectMessage || !isMetaConnectDeliveryOnly())
          sendQueuedMessages()
      }

      // TODO not totally sure what this does, it appears to propagate
      // /meta/* messages (see #extendSend)
      reply = getBayeux().extendReply(serverSession, serverSession, reply)
      sendReply(reply)

      if (isHandshake) {
        // immediately finish
        writer.close()
        info(" >>>> [closed]")
      }

      message.setAssociated(null)
    }

    return serverSession
  }

  private def suspendSession() {
    info("SUSPENDING SESSION")
    val timeout = computeTimeout()
    info("TIMEOUT FOR: ", timeout)

    if (timeout > 0) {
      val continuation = ContinuationSupport.getContinuation(request)
      continuation.setTimeout(timeout + HttpStreamingTransportHandler.JETTY_TIMEOUT_BUFFER)

      var scheduler = serverSession.getAttribute("SCHEDULER").asInstanceOf[HttpStreamingScheduler]
      if (scheduler == null) {
        System.err.println("!!!!! CREATING NEW SCHEDULER")
        scheduler = new HttpStreamingScheduler(serverSession, continuation, this)
        serverSession.setTimeout(timeout)
        serverSession.setScheduler(scheduler)

        serverSession.setAttribute("SCHEDULER", scheduler)
      } else {
        System.err.println("!!!!! SCHEDULER ALREADY EXISTS")
        serverSession.setTimeout(timeout)
      }

      request.setAttribute(HttpStreamingTransportHandler.SCHEDULER_ATTRIBUTE, scheduler)
      request.setAttribute(HttpStreamingTransportHandler.REQUEST_TICK_ATTRIBUTE, requestStartTick)

      continuation.suspend(response)

      info("serverSession timeout: ", serverSession.getTimeout(), "  interval: ", serverSession.getInterval())
    } else {
      val continuation = ContinuationSupport.getContinuation(request)
      continuation.suspend(response)
      continuation.complete()
    }
  }

  private def computeTimeout() : Long = {
    val baseTimeout = transport.getTimeout()

    val delta : Long = (System.nanoTime() - requestStartTick) / 1000000 
    delta match {
      case _ if delta > baseTimeout => 0
      case _ if delta < 0           => 0
      case _                        => baseTimeout - delta
    }
  }

  private def sendQueuedMessages() {
    if (serverSession != null) {
      val queue = serverSession.takeQueue()
      for (message <- queue) {
        sendReply(message)
      }
    }
  }

  private def isMetaConnectDeliveryOnly() = {
    transport.isMetaConnectDeliveryOnly() || serverSession.isMetaConnectDeliveryOnly()
  }

  private def sendReply(reply : ServerMessage) {
    if (reply == null)
      return
    //
    // Map<String, Object> adviceFields = reply.asMutable().getAdvice(true)
    //
    // adviceFields.put(Message.INTERVAL_FIELD, computeTimeout())
    // if (serverSession != null)
    // serverSession.reAdvise()

    writeMessage(reply.getJSON())
  }

  /**
   * send some white space down the front of the reply to get the continuation
   * into a non-inital state.
   */
  private def padInitialReply() {
    try {
      info("PADDED INTIAL REPLY")
      response.getWriter().write("                        ")
    } catch {
      case e : IOException =>
        System.out.println("Could not write message: ")
        e.printStackTrace(System.err)
    }
  }

  private def writeMessage(message : String) {
    if (message == null)
      return

    try {
      if (writer == null)
        writer = new JSONStreamPrintWriter(response.getWriter())

      writer.write(message)
      info(" >>>> ", message)

      flushResponse()
    } catch {
      case e : IOException =>
        System.out.println("Could not successfully write message: ")
        e.printStackTrace(System.err)
    }
  }

  private def finishResponse() {
    if (writer == null)
      return

    writer.close()
  }

  private def flushResponse() {
    if (writer == null)
      return

    try {
      writer.flush()
      response.flushBuffer()
      info(" >>>> [flushed]")
    } catch {
      case e : IOException =>
        info("Could not flush HTTP response buffer: ", e)
    }
  }

  private def handleHandshake(reply : ServerMessage) {
    info("handling handshake")
    serverSession = retrieveSession(reply.getClientId())

    // get the user agent
    if (serverSession != null)
      serverSession.setUserAgent(request.getHeader(HttpStreamingTransportHandler.USER_AGENT_HEADER))
  }

  /**
   * @return the ServerSession associated with the given clientId, null if no
   *         such sesion exists
   */
  private def retrieveSession(clientId : String) =
    getBayeux().getSession(clientId).asInstanceOf[ServerSessionImpl]

  /**
   * @return true if the given clientId does not match the given
   *         serverSession.
   */
  private def isClientSessionMismatch(clientId : String, serverSession : ServerSessionImpl) =
    clientId != null && !clientId.equals(serverSession.getId())
}
