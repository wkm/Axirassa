
package com.zanoccio.axirassa.util;

import org.hibernate.Session;
import org.hibernate.cfg.AnnotationConfiguration;
import org.junit.BeforeClass;

public class AbstractDomainTest {
	public static Session session;


	@BeforeClass
	public static void hibernateSession() {
		AnnotationConfiguration config = new AnnotationConfiguration().configure();
		config.setProperty("hibernate.hbm2ddl.auto", "create-drop");
		config.setProperty("hibernate.show_sql", "true");

		HibernateTools.setSessionFactory(config.buildSessionFactory());

		session = HibernateTools.getSession();
	}


	public static void addEntity(Object entity) {
		session.save(entity);
	}
}
