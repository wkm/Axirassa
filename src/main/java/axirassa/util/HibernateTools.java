
package axirassa.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateTools {
	private static SessionFactory sessionfactory;


	private static SessionFactory buildSessionFactory() {
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
