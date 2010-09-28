
package com.zanoccio.axirassa.util;

import java.util.Collection;
import java.util.Iterator;

import org.apache.tapestry5.json.JSONLiteral;

public class JSONConstructor {
	public static final int BUFFER_SIZE = 1024 * 1024;


	public static JSONLiteral generate(Double[][][] array) {
		StringBuilder sb = new StringBuilder(BUFFER_SIZE);
		sb.append('[');
		for (int i = 0; i < array.length; i++) {
			sb.append('[');
			for (int j = 0; j < array[i].length; j++) {

				sb.append('[');
				for (int k = 0; k < array[i][j].length; k++) {
					sb.append(array[i][j][k]);

					if (k < array[i][j].length - 1)
						sb.append(',');
				}
				sb.append(']');

				if (j < array[i].length - 1)
					sb.append(',');
			}
			sb.append(']');

			if (i < array.length - 1)
				sb.append(',');
		}
		sb.append(']');

		return new JSONLiteral(sb.toString());
	}


	public static <T> JSONLiteral generate(Collection<T> data) {
		StringBuilder sb = new StringBuilder(BUFFER_SIZE);
		Iterator<T> iter = data.iterator();

		sb.append('[');
		while (iter.hasNext()) {
			sb.append(iter.next());

			if (iter.hasNext())
				sb.append(',');
		}
		sb.append(']');

		return new JSONLiteral(sb.toString());
	}


	public static JSONLiteral generateStrings(Collection<String> data) {
		StringBuilder sb = new StringBuilder(BUFFER_SIZE);
		Iterator<String> iter = data.iterator();

		sb.append('[');
		while (iter.hasNext()) {
			String val = iter.next();
			val = val.replace("\\", "\\\\");
			val = val.replace("'", "\\;");

			sb.append('\'');
			sb.append(val);
			sb.append('\'');

			if (iter.hasNext())
				sb.append(',');
		}
		sb.append(']');

		return new JSONLiteral(sb.toString());
	}
}
