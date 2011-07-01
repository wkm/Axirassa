
package axirassa.util.test;

import lombok.Getter;

import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.tapestry5.ioc.annotations.Inject;

public class CustomSecurityManagerFactory {
	@Inject
	@Getter
	private WebSecurityManager webSecurityManager;


	public CustomSecurityManagerFactory() {
		// empty constructor
	}
}
