
package axirassa.overlord;


public class OverlordShutdownHook extends Thread {
	private final OverlordMain overlord;


	public OverlordShutdownHook(OverlordMain overlord) {
		this.overlord = overlord;
	}


	@Override
	public void run() {
		overlord.killInstances();
	}
}
