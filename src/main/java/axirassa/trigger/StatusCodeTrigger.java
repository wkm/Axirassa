
package axirassa.trigger;

public class StatusCodeTrigger extends AbstractTrigger {

	private final int code;


	public StatusCodeTrigger (int code) {
		this.code = code;
	}


	public int getCode () {
		return code;
	}

}
