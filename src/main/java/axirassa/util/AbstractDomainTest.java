
package axirassa.util;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.junit.BeforeClass;

public class AbstractDomainTest {
	public static Session session;


	@BeforeClass
	public static void hibernateSession() {
		Configuration config = new Configuration().configure();
		config.setProperty("connection.url", "jdbc:mysql://localhost/axir_test");
		config.setProperty("hibernate.hbm2ddl.auto", "create-drop");
		config.setProperty("hibernate.show_sql", "true");

		HibernateTools.setSessionFactory(config.buildSessionFactory());

		session = HibernateTools.getSession();
	}


	public static void addEntity(Object entity) {
		session.save(entity);
	}
}
