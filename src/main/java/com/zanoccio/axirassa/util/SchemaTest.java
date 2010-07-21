
package com.zanoccio.axirassa.util;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.junit.Test;

import com.zanoccio.axirassa.domain.User;

public class SchemaTest {
	static {
		AnnotationConfiguration config = new AnnotationConfiguration().configure()
		        .setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect")
		        .setProperty("hibernate.connection.driver_class", "org.hsqldb.jdbcDriver")
		        .setProperty("hibernate.connection.url", "jdbc:hsqldb:mem:baseball")
		        .setProperty("hibernate.connection.username", "sa").setProperty("hibernate.connection.password", "")
		        .setProperty("hibernate.connection.pool_size", "1")
		        .setProperty("hibernate.connection.autocommit", "true")
		        .setProperty("hibernate.cache.provider_class", "org.hibernate.cache.HashtableCacheProvider")
		        .setProperty("hibernate.hbm2ddl.auto", "create-drop").setProperty("hibernate.show_sql", "true")
		        .addAnnotatedClass(User.class);
		HibernateUtil.setSessionFactory(config.buildSessionFactory());
	}


	public static void addEntity(Object entity) {
		Session session = HibernateUtil.getSession();
		try {
			Transaction transaction = session.beginTransaction();
			session.save(entity);
			transaction.commit();
		} finally {
			session.close();
		}
	}


	@Test
	public void userPassword() {
		User user = new User();
		user.setEMail("foo@mail.com");
		user.setPassword("blah");
		addEntity(user);
	}
}
