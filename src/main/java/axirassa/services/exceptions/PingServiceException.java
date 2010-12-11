
package axirassa.services.exceptions;

public abstract class PingServiceException extends Exception {
	private static final long serialVersionUID = 1L;


	public PingServiceException(String message) {
		super(message);
	}
}
