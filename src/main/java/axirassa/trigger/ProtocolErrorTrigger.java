
package axirassa.trigger;

public class ProtocolErrorTrigger extends AbstractTrigger {
	private final Throwable cause;


	public ProtocolErrorTrigger (Throwable cause) {
		this.cause = cause;
	}


	public Throwable getCause () {
		return cause;
	}
}
