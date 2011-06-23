
package axirassa.webapp.services.exceptions;

public class AxirassaSecurityException extends Exception {
	private static final long serialVersionUID = 3791577286798538002L;


	public AxirassaSecurityException() {
		super("Unauthorized access attempt");
	}


	public AxirassaSecurityException(String msg) {
		super(msg);
	}
}
