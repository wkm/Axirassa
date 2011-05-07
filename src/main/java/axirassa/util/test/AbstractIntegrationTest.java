
package axirassa.util.test;

import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.hibernate.HibernateSessionSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.PerthreadManager;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.runner.RunWith;

import axirassa.ioc.IocDatabaseIntegrationTestRunner;

@RunWith(IocDatabaseIntegrationTestRunner.class)
public class AbstractIntegrationTest {

	@Inject
	HibernateSessionManager sessionManager;

	@Inject
	HibernateSessionSource sessionSource;

	@Inject
	PerthreadManager threadManager;


	@After
	public void stopSessions() {
		org.hibernate.Session session = sessionManager.getSession();
		Transaction transaction = session.getTransaction();

		if (transaction.isActive() && !transaction.wasCommitted() && !transaction.wasRolledBack()) {
			transaction.rollback();
			session.close();
		}

		threadManager.cleanup();
	}
}
