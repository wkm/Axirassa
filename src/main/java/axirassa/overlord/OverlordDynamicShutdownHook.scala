
package axirassa.overlord

import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.util.Collection

/**
 * Shutdown hook which asks the user via the CLI whether to terminate child
 * processes. (TODO add a timeout and a default behavior)
 *
 * @author wiktor
 */
class OverlordDynamicShutdownHook(val overlord : Overlord) extends Thread {
	val alivethreadcount = 0

	override def run() {
		val instances = overlord.getExecutionInstances()
		for (instance <- instances)
			if (instance.getThread().isAlive())
				alivethreadcount += 1

		if (alivethreadcount > 0)
			try {
				askToTerminate()
			} catch {
				case e : IOException => e.printStackTrace()
			}
	}

	private def askToTerminate() {
		println("Currently monitoring: "+alivethreadcount+" threads")
		printf("Do you with to terminate threads [Y/n]: ")
		Console.flush()

		val reader = new InputStreamReader(new BufferedInputStream(System.in))

		reader.read() match {
			case 'n' => // ignore
			case _ => overlord.killInstances()
		}
	}
}
