
package axirassa.webapp.pages.trigger;

import org.apache.tapestry5.annotations.Property;

public class CreateTrigger {
	@Property
	private String latency;

	@Property
	private String responseTime;

	@Property
	private String responseSize;
}
