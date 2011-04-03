
package axirassa.ioc;

import org.apache.tapestry5.ioc.MappedConfiguration;
import org.hibernate.Session;

import axirassa.util.HibernateTools;

public class HibernateTestingModule {
	private static Session database;


	private static Session getHibernateSession () {
		if (database == null) {
			database = HibernateTools.buildTestingSessionFactory().openSession();
		}

		return database;
	}


	public static void contributeServiceOverride (MappedConfiguration<Class, Object> configuration) {
		configuration.add(Session.class, getHibernateSession());
	}
}
