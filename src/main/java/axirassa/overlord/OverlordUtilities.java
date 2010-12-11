
package axirassa.overlord;

public class OverlordUtilities {
	public final static String VALID_NAME_PATTERN = "[a-zA-Z_][a-zA-Z0-9_]*";


	/**
	 * @return true if the given name matches {@value #VALID_NAME_PATTERN}.
	 */
	public static boolean isValidName(String name) {
		return name.matches(VALID_NAME_PATTERN);
	}
}
