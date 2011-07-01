
package axirassa.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class CSVResponse extends CompressableTextResponse {
	private static final String CHARSET = "UTF-8";


	public void setResponseData(List<List<Object>> array) throws IOException {
		setResponseData(array, new SimpleCSVRowWriter<List<Object>>() {
			@Override
			public void writeRow(List<Object> row, StringBuilder sb) {
				for (Object cell : row)
					writeCell(sb, cell);
			}
		});
	}


	public <T> void setResponseData(List<T> list, CSVRowWriter<T> rowWriter) throws UnsupportedEncodingException,
	        IOException {
		StringBuilder sb = new StringBuilder();

		for (T row : list) {
			rowWriter.startRow();
			rowWriter.writeRow(row, sb);
			rowWriter.endRow();
			sb.append('\n');
		}

		setResponseData(sb.toString().getBytes(CHARSET));
	}


	@Override
	public String getContentType() {
		return "text/csv; charset=" + CHARSET;
	}
}
