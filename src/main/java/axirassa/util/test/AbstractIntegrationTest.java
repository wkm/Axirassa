
package axirassa.util.test;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import axirassa.ioc.IocDatabaseIntegrationTestRunner;

@RunWith(IocDatabaseIntegrationTestRunner.class)
public class AbstractIntegrationTest {

	@Inject
	HibernateCleanupService cleanupService;


	@Before
	public void wipeAndCreate() {
		cleanupService.wipeAndCreateDB();
	}


	@After
	public void stopSessions() {
		cleanupService.cleanup();
	}
}
