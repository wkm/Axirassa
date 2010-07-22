
package com.zanoccio.axirassa.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.junit.Test;

import com.zanoccio.axirassa.domain.User;
import com.zanoccio.axirassa.domain.exception.NoSaltException;

public class SchemaTest {
	static {
		AnnotationConfiguration config = new AnnotationConfiguration().configure()
		        .setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect")
		        .setProperty("hibernate.connection.driver_class", "org.hsqldb.jdbcDriver")
		        .setProperty("hibernate.connection.url", "jdbc:hsqldb:mem:axir")
		        .setProperty("hibernate.connection.username", "sa").setProperty("hibernate.connection.password", "")
		        .setProperty("hibernate.connection.pool_size", "1")
		        .setProperty("hibernate.connection.autocommit", "true")
		        .setProperty("hibernate.cache.provider_class", "org.hibernate.cache.HashtableCacheProvider")
		        .setProperty("hibernate.hbm2ddl.auto", "create-drop").setProperty("hibernate.show_sql", "true");
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
	public void userPassword() throws NoSaltException {
		User user = new User();
		user.setEMail("foo@mail.com");
		user.setSalt("tweedledee");
		user.createPassword("blah");
		addEntity(user);

		assertTrue(user.matchPassword("blah"));
		assertFalse(user.matchPassword("tweedle"));

		Session session = HibernateUtil.getSession();
		session.beginTransaction();

		Query query = session.createQuery("from User");
		List results = query.list();
		User storeduser = (User) results.get(0);
		session.getTransaction().commit();

		assertTrue(storeduser.matchPassword("blah"));
		assertFalse(storeduser.matchPassword("tweedle"));
		assertFalse(storeduser.matchPassword("*!@#HJKNMoiu9"));
	}
}
