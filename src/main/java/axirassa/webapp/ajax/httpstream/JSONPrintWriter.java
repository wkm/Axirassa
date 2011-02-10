
package axirassa.webapp.ajax.httpstream;

import java.io.PrintWriter;

public class JSONPrintWriter {
	private final PrintWriter writer;
	private int messageCount = 0;


	public JSONPrintWriter(PrintWriter writer) {
		this.writer = writer;
	}


	public void write(String json) {
		if (messageCount == 0)
			writer.append('[');
		else
			writer.append(',');

		writer.write(json);

		messageCount++;
	}


	public void flush() {
		writer.flush();
	}


	public void close() {
		if (messageCount > 0)
			writer.append(']');

		writer.flush();
		writer.close();
	}
}
