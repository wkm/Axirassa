
package zanoccio.javakit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class StringUtilities {
	/**
	 * Read in an entire InputStream as a UTF-8 encoded String.
	 * 
	 * @throws IOException
	 */
	public static String stringFromStream(InputStream is) throws IOException {
		return stringFromStream(is, 4096);
	}


	public static String stringFromStream(InputStream is, int bufferSize) throws IOException {
		if (is == null)
			return "";

		StringBuffer sb = new StringBuffer();
		char[] buffer = new char[bufferSize];

		try {
			Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), bufferSize);
			int bytesRead = 0;

			while ((bytesRead = reader.read(buffer)) != -1) {
				sb.append(buffer, 0, bytesRead);
			}
		} finally {
			is.close();
		}

		return sb.toString();
	}

}
