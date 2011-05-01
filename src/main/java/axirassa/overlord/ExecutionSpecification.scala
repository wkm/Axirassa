
package axirassa.overlord

import java.io.File
import java.io.IOException
import java.util.Collection

import lombok.Getter
import lombok.Setter

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
			val execId = configuration.overlord.getNextExecID()
			printf("  %s %d -- %s\n", target.getName(), execId, target.getTargetClass().getCanonicalName())
			executeInstance(execId)
		}
	}

	private def executeInstance(id : Int) {
		provideLibraries()

		val cli = new CommandLine(configuration.getJavaExecutable())

		// add classpath if applicable
		if (configuration.getClassPath() != null) {
			cli.addArgument("-cp")
			cli.addArgument(configuration.getClassPath())
		} else {
			cli.addArgument("-cp")
			cli.addArgument(System.getProperty("java.class.path"))
		}

		// add jvm options
		cli.addArguments(target.getOptions().getCommandLine())

		// set the library path, if applicable
		if (target.getOptions().needsLibraries()) {
			val libprovider = configuration.overlord.getNativeLibraryProvider()

			val path = libprovider.getLibraryPath()
			if (path != null)
				cli.addArgument("-Djava.library.path="+path)
		}

		// add class name
		cli.addArgument(target.getTargetClass().getCanonicalName())

		val processbuilder = new ProcessBuilder(cli.buildCommandLine)
		processbuilder.redirectErrorStream(true)
		processbuilder.directory(new File(configuration.getBaseDirectory()))

		val monitor = new ExecutionMonitor(target, id, processbuilder)
		monitor.remainingRestarts = -1

		val thread = new Thread(monitor, "overlord-monitor "+target+"["+id+"]")
		configuration.overlord.addExecutionInstance(thread, monitor)
		thread.start()
	}

	private def provideLibraries() {
		val libraries = target.getOptions().getLibraries()
		val libprovider = configuration.overlord.getNativeLibraryProvider()

		libraries.map(library => libprovider.provideLibrary(library))
	}
}
