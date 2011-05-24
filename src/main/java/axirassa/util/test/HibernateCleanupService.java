
package axirassa.util.test;

import org.apache.tapestry5.hibernate.HibernateSessionSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.PerthreadManager;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.Dialect;

public class HibernateCleanupService {
	//
	// @Inject
	// HibernateSessionManager sessionManager;

	@Inject
	HibernateSessionSource sessionSource;

	@Inject
	PerthreadManager threadManager;


	public void cleanup() {
		Session session = sessionSource.getSessionFactory().openSession();
		Transaction transaction = session.getTransaction();

		if (transaction.isActive() && !transaction.wasCommitted() && !transaction.wasRolledBack()) {
			transaction.rollback();
			session.close();
		}

		threadManager.cleanup();
	}


	public void wipeAndCreateDB() {

		Session session = sessionSource.getSessionFactory().openSession();

		Configuration configuration = sessionSource.getConfiguration();
		Dialect dialect = Dialect.getDialect(configuration.getProperties());

		String[] dropSQL = configuration.generateDropSchemaScript(dialect);
		String[] createSQL = configuration.generateSchemaCreationScript(dialect);

		session.beginTransaction();

		for (String sql : dropSQL)
			session.createSQLQuery(sql).executeUpdate();

		for (String sql : createSQL)
			session.createSQLQuery(sql).executeUpdate();

		if (session.getTransaction().isActive()) {
			session.getTransaction().commit();
			session.getTransaction().begin();
		}
	}
}
