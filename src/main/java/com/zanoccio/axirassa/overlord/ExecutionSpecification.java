
package com.zanoccio.axirassa.overlord;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class ExecutionSpecification {
	//
	// Class Instances
	//

	private final OverlordConfiguration configuraton;
	private int instances;
	private final ExecutionTarget target;

	private URL basedirectory;
	private String javaexec;


	public ExecutionSpecification(OverlordConfiguration configuraton, ExecutionTarget target) {
		this.configuraton = configuraton;
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
		ArrayList<String> cli = new ArrayList<String>();
		cli.add(configuraton.getJavaExecutable());

		// add jvm options
		if (target.getJVMOptions().size() > 0) {
			cli.addAll(target.getJVMOptions().getCommandLine());
		}

		cli.add(target.getTargetClass().getCanonicalName());

		ProcessBuilder processbuilder = new ProcessBuilder(cli);
		processbuilder.redirectErrorStream(true);
		processbuilder.directory(new File(configuraton.getBaseDirectory()));

		Process process = processbuilder.start();
	}
}
