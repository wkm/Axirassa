
package com.zanoccio.axirassa.overlord;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;

/**
 * Shutdown hook which asks the user via the CLI whether to terminate child
 * processes. (TODO add a timeout and a default behavior)
 * 
 * @author wiktor
 */
public class OverlordDynamicShutdownHook extends Thread {

	private int timeout;
	private boolean terminateByDefault;
	private final Overlord overlord;
	private int alivethreadcount = 0;


	public OverlordDynamicShutdownHook(Overlord overlord) {
		this.overlord = overlord;
	}


	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}


	public void setTerminateByDefault(boolean terminateByDefault) {
		this.terminateByDefault = terminateByDefault;
	}


	@Override
	public void run() {
		Collection<ExecutionInstance> instances = overlord.getExecutionInstances();
		for (ExecutionInstance instance : instances)
			if (instance.getThread().isAlive())
				alivethreadcount++;

		if (alivethreadcount > 0)
			try {
				askToTerminate();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}


	private void askToTerminate() throws IOException {
		System.out.println("Currently monitoring: " + alivethreadcount + " threads");
		System.out.printf("Do you with to terminate threads [Y/n]: ").flush();

		InputStreamReader reader = new InputStreamReader(new BufferedInputStream(System.in));
		int key = reader.read();
		switch (key) {
		case 'n':
			break;

		default:
			terminateThreads();
			break;
		}
	}


	private void terminateThreads() {
		for (ExecutionInstance instance : overlord.getExecutionInstances())
			if (instance.getThread().isAlive()) {
				instance.getThread().interrupt();
				System.out.println("  Killing process");
				instance.getMonitor().killProcess();
			}
	}
}
