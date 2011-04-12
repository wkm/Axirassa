
package axirassa.trigger;

import lombok.Getter;

public class PingerErrorTrigger extends AbstractTrigger {
	@Getter
	private final Throwable cause;


	public PingerErrorTrigger (Throwable cause) {
		this.cause = cause;
	}
}
