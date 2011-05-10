
package axirassa.util.test;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.realm.Realm;
import org.tynamo.security.services.TapestryRealmSecurityManager;

import axirassa.ioc.AxirassaSecurityModule;
import axirassa.ioc.DAOModule;
import axirassa.ioc.ExternalServicesMockingModule;
import axirassa.ioc.FlowsModule;
import axirassa.ioc.LoggingModule;
import axirassa.ioc.MessagingModule;

import com.formos.tapestry.testify.core.TapestryTester;
import com.formos.tapestry.testify.junit4.TapestryTest;

public class TapestryPageTest extends TapestryTest {
	private static final TapestryTester SHARED_TESTER = new TapestryTester("axirassa.webapp", DAOModule.class,
	        FlowsModule.class, MessagingModule.class, LoggingModule.class, ExternalServicesMockingModule.class,
	        AxirassaSecurityModule.class);


	public TapestryPageTest() {
		super(SHARED_TESTER);

		setSecurityManager();
	}


	private void setSecurityManager() {
		System.out.println("NO SECURITY MANAGER; CREATING IT");
		CustomSecurityManagerFactory factory = SHARED_TESTER.autobuild(CustomSecurityManagerFactory.class);

		Collection<Realm> realms = new ArrayList<Realm>();
		realms.add(factory.getEntityRealm());

		org.apache.shiro.mgt.SecurityManager manager = new TapestryRealmSecurityManager(realms);
		SecurityUtils.setSecurityManager(manager);
	}
}
