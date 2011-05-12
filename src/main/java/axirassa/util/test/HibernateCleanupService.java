
package axirassa.util.test;

import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.hibernate.HibernateSessionSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.PerthreadManager;
import org.hibernate.Transaction;

public class HibernateCleanupService {

	@Inject
	HibernateSessionManager sessionManager;

	@Inject
	HibernateSessionSource sessionSource;

	@Inject
	PerthreadManager threadManager;


	public void cleanup() {
		org.hibernate.Session session = sessionManager.getSession();
		Transaction transaction = session.getTransaction();

		if (transaction.isActive() && !transaction.wasCommitted() && !transaction.wasRolledBack()) {
			transaction.rollback();
			session.close();
		}

		threadManager.cleanup();
	}
}
