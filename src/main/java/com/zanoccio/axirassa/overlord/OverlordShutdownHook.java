
package com.zanoccio.axirassa.overlord;


public class OverlordShutdownHook extends Thread {
	private final Overlord overlord;


	public OverlordShutdownHook(Overlord overlord) {
		this.overlord = overlord;
	}


	@Override
	public void run() {
		overlord.killInstances();
	}
}
