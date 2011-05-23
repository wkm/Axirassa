
package axirassa.ioc;

import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.tapestry5.ioc.annotations.Match;
import org.tynamo.security.services.TapestryRealmSecurityManager;

import axirassa.util.test.RealmTestSecurityManager;

public class PageTestingModule {
	@Match("WebSecurityManager")
	static public Object decorateWebSecurityManager(WebSecurityManager delegate) {
		if (delegate instanceof TapestryRealmSecurityManager) {
			TapestryRealmSecurityManager castDelegate = (TapestryRealmSecurityManager) delegate;
			return new RealmTestSecurityManager(castDelegate.getRealms());
		} else {
			return delegate;
		}
	}
}
