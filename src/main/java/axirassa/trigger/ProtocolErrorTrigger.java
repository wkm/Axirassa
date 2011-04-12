
package axirassa.trigger;

import lombok.Getter;

public class ProtocolErrorTrigger extends AbstractTrigger {
	@Getter
	private final Throwable cause;


	public ProtocolErrorTrigger (Throwable cause) {
		this.cause = cause;
	}
}
