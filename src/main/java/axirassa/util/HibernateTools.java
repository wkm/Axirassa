
package axirassa.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateTools {
	private static SessionFactory sessionfactory;


	public static SessionFactory buildSessionFactory() {
		try {
			Configuration config = new Configuration().configure();
			config.setProperty("hibernate.c3p0.min_size", "1");
			config.setProperty("hibernate.c3p0.max_Size", "1");

			return config.buildSessionFactory();

		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}


	/**
	 * Mutably modifies the given configuration for testing:
	 * <ul>
	 * <li>use a testing database
	 * <li>drop and create the testing schema
	 * <li>show SQL as it executes
	 * <li>do not use c3p0 connection pooling
	 * </ul>
	 */
	public static Configuration configureTesting(Configuration config) {
		config.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost/axir_test");
		config.setProperty("hibernate.hbm2ddl.auto", "create-drop");
		config.setProperty("hibernate.show_sql", "true");

		// disable c3p0
		config.setProperty("hibernate.c3p0.min_size", "");
		config.setProperty("hibernate.c3p0.max_size", "");
		config.setProperty("hibernate.c3p0.timeout", "");
		config.setProperty("hibernate.c3p0.max_statements", "");

		return config;
	}


	/**
	 * builds and returns a {@link SessionFactory} connected to the
	 * <tt>axir_test</tt> schema and set to drop and create the database schema
	 * on connect.
	 */
	public static SessionFactory buildTestingSessionFactory() {
		Configuration config = new Configuration();
		config.configure();

		configureTesting(config);

		return config.buildSessionFactory();
	}


	public static SessionFactory getSessionFactory() {
		if (sessionfactory == null)
			sessionfactory = buildSessionFactory();

		return sessionfactory;
	}


	public static void setSessionFactory(SessionFactory buildSessionFactory) {
		sessionfactory = buildSessionFactory;
	}


	public static Session getLightweightSession() {
		return getSessionFactory().openSession();
	}
}
