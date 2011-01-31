
package axirassa.overlord;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import axirassa.overlord.exceptions.ExceptionInMonitorError;

/**
 * A lightweight thread that monitors a process, restarting it if crashes.
 * 
 * @author wiktor
 */
public class ExecutionMonitor implements Runnable {
	private final ExecutionTarget target;
	private final int id;
	private int remainingRestarts = 0;
	private int startCount = 0;
	private final ProcessBuilder builder;
	private Process process;


	public ExecutionMonitor(ExecutionTarget target, int id, ProcessBuilder builder) {
		this.target = target;
		this.id = id;

		this.builder = builder;
	}


	public void setRemainingRestarts(int remainingRestarts) {
		this.remainingRestarts = remainingRestarts;
	}


	public int getRemainingRestarts() {
		return remainingRestarts;
	}


	@Override
	public void run() {
		while (remainingRestarts > 0) {
			try {
				System.out.printf(toString() + " starting [%d]: " + builder.command() + "\n", startCount);
				process = builder.start();

				BufferedReader stdoutstream = new BufferedReader(new InputStreamReader(process.getInputStream()));
				BufferedReader stderrstream = new BufferedReader(new InputStreamReader(process.getErrorStream()));

				String line;
				while ((line = stdoutstream.readLine()) != null)
					System.out.println(getId() + " STDOUT: " + line);
				while ((line = stderrstream.readLine()) != null)
					System.out.println(getId() + " STDERR: " + line);

				startCount++;
				remainingRestarts--;

				process.waitFor();
			} catch (InterruptedException e) {
				System.out.println("ExecutionMonitor interrupted.");
				return;
			} catch (Exception e) {
				throw new ExceptionInMonitorError(e);
			}
		}
		System.out.println(toString() + " finished.");
	}


	public void killProcess() {
		if (process == null)
			return;

		System.out.println("  Killing process");
		process.destroy();
	}


	private String getId() {
		return target.getName() + " " + id;
	}
}
