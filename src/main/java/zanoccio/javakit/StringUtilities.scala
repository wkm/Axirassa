
package zanoccio.javakit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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


	public static String removeLeadingWhitespace(String content) {
		Pattern pattern = Pattern.compile("(?m)^([ \t]*)(.*)$");
		Matcher matcher = pattern.matcher(content);

		StringBuilder stripped = new StringBuilder();

		while (matcher.find()) {
			stripped.append(matcher.group(2));
			stripped.append('\n');
		}

		return stripped.toString().trim();
	}
}
