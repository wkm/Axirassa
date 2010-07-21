
package com.zanoccio.axirassa.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public class HibernateUtil {
	private static SessionFactory sessionfactory = buildSessionFactory();


	private static SessionFactory buildSessionFactory() {
		try {
			return new AnnotationConfiguration().configure().buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}


	public static SessionFactory getSessionFactory() {
		return sessionfactory;
	}


	public static void setSessionFactory(SessionFactory buildSessionFactory) {
		sessionfactory = buildSessionFactory;
	}


	public static Session getSession() {
		return sessionfactory.openSession();
	}
}
