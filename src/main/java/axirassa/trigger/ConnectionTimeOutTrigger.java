
package axirassa.trigger;

import java.net.SocketException;

import lombok.Getter;

public class ConnectionTimeOutTrigger extends AbstractTrigger {

	@Getter
	private SocketException cause;


	public ConnectionTimeOutTrigger (SocketException cause) {
		this.cause = cause;
	}

}
