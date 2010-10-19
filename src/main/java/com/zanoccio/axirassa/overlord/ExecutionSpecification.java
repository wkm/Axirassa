
package com.zanoccio.axirassa.overlord;

import java.io.File;
import java.io.IOException;

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
			executeInstance();
		}
	}


	private void executeInstance() throws IOException {
		CommandLine cli = new CommandLine(configuration.getJavaExecutable());

		// add jvm options
		if (target.getJVMOptions().size() > 0)
			cli.addArguments(target.getJVMOptions().getCommandLine());

		cli.addArgument(target.getTargetClass().getCanonicalName());

		ProcessBuilder processbuilder = new ProcessBuilder(cli.buildCommandLine());
		processbuilder.redirectErrorStream(true);
		processbuilder.directory(new File(configuration.getBaseDirectory()));

		ExecutionMonitor monitor = new ExecutionMonitor(processbuilder);
		monitor.setRemainingRestarts(5);
		Thread thread = new Thread(monitor);
		thread.start();
	}
}
