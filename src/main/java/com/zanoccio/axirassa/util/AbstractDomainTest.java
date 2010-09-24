
package com.zanoccio.axirassa.util;

import org.hibernate.Session;
import org.hibernate.cfg.AnnotationConfiguration;
import org.junit.BeforeClass;

public class AbstractDomainTest {
	public static Session session;


	@BeforeClass
	public static void hibernateSession() {
		AnnotationConfiguration config = new AnnotationConfiguration().configure()
		// .setProperty("hibernate.dialect",
		// "org.hibernate.dialect.HSQLDialect")
		// .setProperty("hibernate.connection.driver_class",
		// "org.hsqldb.jdbcDriver")
		// .setProperty("hibernate.connection.url", "jdbc:hsqldb:mem:axir")
		// .setProperty("hibernate.connection.username",
		// "sa").setProperty("hibernate.connection.password", "")
		// .setProperty("hibernate.connection.pool_size", "1")
		// .setProperty("hibernate.connection.autocommit", "true")
		// .setProperty("hibernate.cache.provider_class",
		// "org.hibernate.cache.HashtableCacheProvider")
		        .setProperty("hibernate.hbm2ddl.auto", "create-drop").setProperty("hibernate.show_sql", "true");
		HibernateTools.setSessionFactory(config.buildSessionFactory());

		session = HibernateTools.getSession();
	}


	public static void addEntity(Object entity) {
		session.save(entity);
	}
}
