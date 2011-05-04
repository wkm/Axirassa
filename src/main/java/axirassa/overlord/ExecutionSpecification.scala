
package axirassa.overlord

import java.io.File
import scala.collection.JavaConversions._

class ExecutionSpecification(
	var configuration : OverlordConfiguration,
	var target : ExecutionTarget) {

	var instances = 0
	var initialDelay = 0

	def execute() {
		if (initialDelay > 0) {
			Thread.sleep(initialDelay)
		}

		for (i <- 0 until instances) {
			val execId = Overlord.getNextExecID()
			printf("  %s %d -- %s\n", target.name, execId, target.targetClass.getCanonicalName())
			executeInstance(execId)
		}
	}

	private def executeInstance(id : Int) {
		provideLibraries()

		val cli = new CommandLine(configuration.javaExecutable)

		// add classpath if applicable
		if (configuration.classPath != null) {
			cli.addArgument("-cp")
			cli.addArgument(configuration.classPath)
		} else {
			cli.addArgument("-cp")
			cli.addArgument(System.getProperty("java.class.path"))
		}

		// add jvm options
		cli.addArguments(target.options.getCommandLine)

		// set the library path, if applicable
		if (target.options.needsLibraries) {
			val libprovider = configuration.overlord.libraryProvider

			val path = libprovider.getLibraryPath()
			if (path != null)
				cli.addArgument("-Djava.library.path="+path)
		}

		// add class name
		cli.addArgument(target.targetClass.getCanonicalName())

		val processbuilder = new ProcessBuilder(cli.buildCommandLine)
		processbuilder.redirectErrorStream(true)
		processbuilder.directory(new File(configuration.baseDirectory))

		val monitor = new ExecutionMonitor(target, id, processbuilder)
		monitor.remainingRestarts = -1

		val thread = new Thread(monitor, "overlord-monitor "+target+"["+id+"]")
		configuration.overlord.addExecutionInstance(thread, monitor)
		thread.start()
	}

	private def provideLibraries() {
		val libraries = target.options.libraries
		val libprovider = configuration.overlord.libraryProvider

		libraries.map(library => libprovider.provideLibrary(library))
	}
}
