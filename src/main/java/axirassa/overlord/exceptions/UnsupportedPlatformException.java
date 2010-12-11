
package axirassa.overlord.exceptions;

public class UnsupportedPlatformException extends OverlordException {
	private static final long serialVersionUID = 3325591197692511048L;


	public UnsupportedPlatformException(String platform) {
		super(platform + " is not a supported platform for Overlord");
	}
}
