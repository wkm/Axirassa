
package axirassa.webapp.components;

import org.apache.tapestry5.annotations.Parameter;

public class AxFormVSpace {
	@Parameter(required = false, value = "1")
	private int height;


	public int getHeight() {
		return height;
	}
}
