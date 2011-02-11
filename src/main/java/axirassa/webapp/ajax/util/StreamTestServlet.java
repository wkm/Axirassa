
package axirassa.webapp.ajax.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StreamTestServlet extends HttpServlet {
	private static final long serialVersionUID = -7813273099595566041L;


	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/json");

		PrintWriter writer = response.getWriter();

		for (int i = 0; i < 5; i++) {
			writer.printf("\"%s\"\n", new Date().toString());
			writer.flush();
			response.flushBuffer();

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {

			}
		}

		writer.close();
	}
}
