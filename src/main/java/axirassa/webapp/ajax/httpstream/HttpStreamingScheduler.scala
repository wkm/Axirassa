
package axirassa.webapp.ajax.httpstream

import org.cometd.server.AbstractServerTransport.Scheduler
import org.cometd.server.ServerSessionImpl
import org.eclipse.jetty.continuation.Continuation
import org.eclipse.jetty.continuation.ContinuationListener

class HttpStreamingScheduler(
  serverSession : ServerSessionImpl,
  continuation : Continuation,
  handler : HttpStreamingTransportHandler) extends ContinuationListener with Scheduler {

  // force a new request just before a minute passes (to keep middle-proxies
  // happy)
  val MAX_SESSION_LENGTH = 55000
  continuation.addContinuationListener(this)

  def getServerSession() = serverSession

  //
  // the general strategy here is to stop the request and let the client
  // reconnect
  //

  override def onComplete(continuation : Continuation) {
    // nothing to do
  }

  override def onTimeout(continuation : Continuation) {
    // remove the scheduler, the client has to reconnect
    System.err.println("REMOVING SCHEDULER FROM SERVER SESSION")
    serverSession.setScheduler(null)
  }

  override def cancel() {
    if (continuation != null && continuation.isSuspended() && !continuation.isExpired()) {
      System.err.println("CANCELLING CONTINUATION")

      try {
        continuation.complete()
      } catch {
        case e : Exception => System.out.println("Exception during scheduler #cancel: "+e.getMessage())
      }
    }
  }

  override def schedule() {
    continuation.resume()
  }

  def getContinuation() = continuation
}
