package axirassa.overlord


import org.slf4j.LoggerFactory
import io.Source


/**
 * A lightweight thread that monitors a process, restarting it if crashes.
 *
 * @author wiktor
 */
class ExecutionMonitor (
                         var target: ExecutionTarget,
                         val id: Int,
                         val builder: ProcessBuilder) extends Runnable {

  val monitorLogger = LoggerFactory.getLogger(classOf[ExecutionMonitor])

  var limitedRestarts = false

  var remainingRestarts = 0
  var startCount        = 0
  var process: Process  = null


  override def run () {
    val logger = LoggerFactory.getLogger(target.targetClass)

    while (remainingRestarts > 0 || limitedRestarts == false) {
      try { {
        monitorLogger.info("STARTING [{}]: {}", startCount, builder.command())
        process = builder.start()

        val stdinLines = Source.fromInputStream(process.getInputStream, "UTF-8").getLines
        stdinLines.foreach {
          line => logger.info("{} : {}", getId, line)
        }

        val stderrLines = Source.fromInputStream(process.getErrorStream, "UTF-8").getLines
        stderrLines.foreach {
          line => logger.warn("{} : {}", getId, line)
        }

        startCount += 1
        remainingRestarts -= 1

        process.waitFor()

        if (!target.autoRestart)
          return
      }
      } catch {
        case e: InterruptedException => {
          monitorLogger.warn("ExecutionMonitor interrupted.")
          return
        }
        case e: Exception => {
          throw new ExceptionInMonitorError(e)
        }
      }
    }

    monitorLogger.info("[{}] finished.", startCount)
  }


  def killProcess () {
    if (process == null)
      return

    monitorLogger.info("  Killing process")
    process.destroy()
  }


  private def getId = "[" + id + "]"
}
