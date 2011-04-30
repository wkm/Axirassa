
package axirassa.webapp.ajax.util

import java.util.concurrent.ThreadFactory

class DaemonThreadFactory extends ThreadFactory {
  override def newThread(r : Runnable) : Thread = {
    val thread = new Thread(r)
    thread.setDaemon(true)
    thread
  }
}
