
package zanoccio.javakit.exception;

public class UnknownResourceException extends Exception {
	private static final long serialVersionUID = 7567841029654859041L;


	public UnknownResourceException(String path) {
		super("Could not find the resource: " + path);
	}
}
