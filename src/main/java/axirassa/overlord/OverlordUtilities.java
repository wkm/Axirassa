
package axirassa.overlord;

import java.io.InputStream;
import java.net.URL;

public class OverlordUtilities {
	public final static String VALID_NAME_PATTERN = "[a-zA-Z_][a-zA-Z0-9_]*";


	public static URL findResource (String name) {
		OverlordUtilities util = new OverlordUtilities();
		URL url = util.getClass().getClassLoader().getResource("/" + name);
		if (url == null)
			url = ClassLoader.getSystemResource(name);

		return url;
	}


	public static InputStream findResourceAsStream (String name) {
		InputStream stream = OverlordUtilities.class.getClassLoader().getResourceAsStream(name);

		if (stream == null)
			stream = ClassLoader.getSystemResourceAsStream(name);

		return stream;
	}


	/**
	 * @return true if the given name matches {@value #VALID_NAME_PATTERN}.
	 */
	public static boolean isValidName (String name) {
		return name.matches(VALID_NAME_PATTERN);
	}


	public static String retrieveJarFile (URL file) {
		String[] components = file.getPath().split("!", 2);

		if (components.length < 2)
			return null;

		if (components[0].toLowerCase().endsWith(".jar"))
			return components[0];

		return null;
	}
}
