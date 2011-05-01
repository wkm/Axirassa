
package axirassa.overlord

/**
 * Represents a period of time over which the number of restarts of a process
 * may be watched.
 *
 * @author wiktor
 *
 */
object MonitorPeriod {
  val MINUTE = new MonitorPeriod(60 * 1000)
  val HOUR = new MonitorPeriod(60 * 60 * 1000)
  val DAY = new MonitorPeriod(24 * 60 * 60 * 1000)
}

class MonitorPeriod(var millis : Long)