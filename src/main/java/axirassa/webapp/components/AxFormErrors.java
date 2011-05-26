
package axirassa.webapp.components;

import org.apache.tapestry5.CSSClassConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;

public class AxFormErrors {
	@Environmental
	private ValidationTracker tracker;

	@Parameter("message:default-banner")
	private String banner;

	@Parameter(name = "class")
	private final String className = CSSClassConstants.ERROR;


	void beginRender(MarkupWriter writer) {
		if (!tracker.getHasErrors())
			return;

		writer.element("div", "class", className);

		writer.element("div");
		writer.write(banner);
		writer.end();

		writer.end();

		for (String error : tracker.getErrors())
			System.err.println("ERROR: " + error);
	}
}
