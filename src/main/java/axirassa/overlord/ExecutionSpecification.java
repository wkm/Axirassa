
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


	public ExecutionSpecification(OverlordConfiguration configuraton, ExecutionTarget target) {
		this.configuration = configuraton;
		this.target = target;
	}


	public void setInstances(int count) {
		if (count < 1)
			return;

		instances = count;
	}


	public int getInstances() {
		return instances;
	}


	public void execute() throws IOException {
		for (int i = 0; i < instances; i++) {
			System.out.printf("  %s %d -- %s\n", target.getName(), i, target.getTargetClass().getCanonicalName());
			executeInstance(i);
		}
	}


	private void executeInstance(int id) throws IOException {
		provideLibraries();

		CommandLine cli = new CommandLine(configuration.getJavaExecutable());

		// add classpath if applicable
		if (configuration.getClassPath() != null) {
			cli.addArgument("-cp");
			cli.addArgument(configuration.getClassPath());
		}

		// add jvm options
		cli.addArguments(target.getOptions().getCommandLine());

		// set the library path, if applicable
		if (target.getOptions().needsLibraries()) {
			System.out.println("NEEDS LIBRARIES");
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
		monitor.setRemainingRestarts(1);

		Thread thread = new Thread(monitor);
		configuration.getOverlord().addExecutionInstance(thread, monitor);
		thread.start();
	}


	private void provideLibraries() throws IOException {
		Collection<String> libraries = target.getOptions().getLibraries();
		NativeLibraryProvider libprovider = configuration.getOverlord().getNativeLibraryProvider();

		for (String library : libraries)
			libprovider.provideLibrary(library);
	}
}
