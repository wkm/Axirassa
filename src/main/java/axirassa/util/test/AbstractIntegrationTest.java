
package axirassa.util.test;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.junit.After;
import org.junit.runner.RunWith;

import axirassa.ioc.IocDatabaseIntegrationTestRunner;

@RunWith(IocDatabaseIntegrationTestRunner.class)
public class AbstractIntegrationTest {

	@Inject
	HibernateCleanupService cleanupService;


	@After
	public void stopSessions() {
		cleanupService.cleanup();
	}
}
