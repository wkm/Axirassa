
package axirassa.trigger;

import java.net.ConnectException;

import lombok.Getter;

public class CouldNotConnectTrigger extends AbstractTrigger {

	@Getter
	private ConnectException cause;


	public CouldNotConnectTrigger (ConnectException cause) {
		this.cause = cause;
	}
}
