
package zanoccio.javakit;

public class ArrayUtilities {
	public static String toString(byte[] array) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < array.length; i++) {
			if (i > 0)
				sb.append('-');

			if (array[i] < 0)
				sb.append(127 + -array[i]);
			else
				sb.append(array[i]);
		}

		return sb.toString();
	}
}
