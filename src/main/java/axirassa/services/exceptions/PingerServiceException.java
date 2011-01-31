
package axirassa.services.exceptions;

abstract public class PingerServiceException extends Exception {
	private static final long serialVersionUID = -6376967583142813656L;


	public PingerServiceException(String string) {
		super(string);
	}
}
