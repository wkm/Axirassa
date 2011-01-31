
package axirassa.services.exceptions;


public class InvalidMessageClassException extends AxirassaServiceException {
	private static final long serialVersionUID = -1456897022165061774L;


	public InvalidMessageClassException(Class<? extends Object> expectedClass, Object receivedObject) {
		super("Expected message object of class: " + expectedClass.getCanonicalName() + " received object: "
		        + receivedObject.getClass().getCanonicalName());
	}

}
