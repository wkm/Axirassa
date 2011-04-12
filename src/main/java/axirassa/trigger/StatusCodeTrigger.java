
package axirassa.trigger;

import lombok.Getter;

public class StatusCodeTrigger extends AbstractTrigger {

	@Getter
	private final int code;


	public StatusCodeTrigger (int code) {
		this.code = code;
	}

}
