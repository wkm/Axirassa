
package axirassa.util.test;

import java.util.Collection;

import org.apache.shiro.realm.Realm;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;

public class RealmTestSecurityManager extends DefaultWebSecurityManager {
	public RealmTestSecurityManager(final Collection<Realm> realms) {
		super(realms);
		setSessionManager(new DefaultWebSessionManager());
	}
}
