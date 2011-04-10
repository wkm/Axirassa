
package axirassa.overlord;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class ExecutionSpecification {
	//
	// Class Instances
	//

	private final OverlordConfiguration configuration;
	private int instances;
	private final ExecutionTarget target;
	private int initialDelay = 0;


	public ExecutionSpecification (OverlordConfiguration configuraton, ExecutionTarget target) {
		this.configuration = configuraton;
		this.target = target;
	}


	public void setInstances (int count) {
		if (count < 1)
			return;

		instances = count;
	}


	public int getInstances () {
		return instances;
	}


	public void execute () throws IOException, InterruptedException {
		if (initialDelay > 0) {
			Thread.sleep(initialDelay);
		}

		for (int i = 0; i < instances; i++) {
			int execId = configuration.getOverlord().getNextExecID();
			System.out.printf("  %s %d -- %s\n", target.getName(), execId, target.getTargetClass().getCanonicalName());
			executeInstance(execId);
		}
	}


	private void executeInstance (int id) throws IOException {
		provideLibraries();

		CommandLine cli = new CommandLine(configuration.getJavaExecutable());

		// add classpath if applicable
		if (configuration.getClassPath() != null) {
			cli.addArgument("-cp");
			cli.addArgument(configuration.getClassPath());
		} else {
			cli.addArgument("-cp");
			cli.addArgument(System.getProperty("java.class.path"));
		}

		// add jvm options
		cli.addArguments(target.getOptions().getCommandLine());

		// set the library path, if applicable
		if (target.getOptions().needsLibraries()) {
			NativeLibraryProvider libprovider = configuration.getOverlord().getNativeLibraryProvider();

			String path = libprovider.getLibraryPath();
			if (path != null)
				cli.addArgument("-Djava.library.path=" + path);
		}

		// add class name
		cli.addArgument(target.getTargetClass().getCanonicalName());

		ProcessBuilder processbuilder = new ProcessBuilder(cli.buildCommandLine());
		processbuilder.redirectErrorStream(true);
		processbuilder.directory(new File(configuration.getBaseDirectory()));

		ExecutionMonitor monitor = new ExecutionMonitor(target, id, processbuilder);
		monitor.setRemainingRestarts(-1);

		Thread thread = new Thread(monitor, "overlord-monitor " + target + "[" + id + "]");
		configuration.getOverlord().addExecutionInstance(thread, monitor);
		thread.start();
	}


	private void provideLibraries () throws IOException {
		Collection<String> libraries = target.getOptions().getLibraries();
		NativeLibraryProvider libprovider = configuration.getOverlord().getNativeLibraryProvider();

		for (String library : libraries)
			libprovider.provideLibrary(library);
	}


	public void setInitialDelay (int initialDelay) {
		this.initialDelay = initialDelay;
	}


	public int getInitialDelay () {
		return initialDelay;
	}
}
