
package axirassa.trigger;

public class PingerErrorTrigger extends AbstractTrigger {
	private final Throwable cause;


	public PingerErrorTrigger (Throwable cause) {
		this.cause = cause;
	}


	public Throwable getCause () {
		return cause;
	}

}
