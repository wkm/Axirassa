
package com.zanoccio.axirassa.overlord;

import com.zanoccio.axirassa.overlord.exceptions.ExceptionInMonitorError;

/**
 * A lightweight thread that monitors a process, restarting it if crashes.
 * 
 * @author wiktor
 */
public class ExecutionMonitor implements Runnable {
	private int remainingRestarts = 0;
	private int startCount = 0;
	private final ProcessBuilder builder;


	public ExecutionMonitor(ProcessBuilder builder) {
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
				Process process = builder.start();

				startCount++;
				remainingRestarts--;

				process.waitFor();
			} catch (Exception e) {
				throw new ExceptionInMonitorError(e);
			}
		}
		System.out.println(toString() + " finished.");
	}
}
