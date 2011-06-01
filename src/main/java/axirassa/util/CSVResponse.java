
package axirassa.util;

import java.io.IOException;
import java.util.List;

public class CSVResponse extends CompressableTextResponse {
	private static final String CHARSET = "UTF-8";


	public CSVResponse(List<List<Object>> array) throws IOException {
		StringBuilder sb = new StringBuilder();

		for (List<Object> row : array) {
			for (Object cell : row) {
				sb.append(cell);
				sb.append(';');
			}
			sb.append('\n');
		}

		setResponseData(sb.toString().getBytes(CHARSET));
	}


	@Override
	public String getContentType() {
		return "text/csv; charset=" + CHARSET;
	}
}
