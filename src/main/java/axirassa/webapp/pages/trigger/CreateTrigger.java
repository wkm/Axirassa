
package axirassa.webapp.pages.trigger;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.tapestry5.annotations.Property;

@RequiresUser
public class CreateTrigger {
	@Property
	private String latency;

	@Property
	private String responseTime;

	@Property
	private String responseSize;
}
