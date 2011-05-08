package axirassa.util.test;

import lombok.Getter;

import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.tapestry5.ioc.annotations.Inject;

public class CustomSecurityManagerFactory {
	@Inject
	@Getter
	private AuthorizingRealm entityRealm;


	public CustomSecurityManagerFactory() {
		// empty constructor
	}
}
