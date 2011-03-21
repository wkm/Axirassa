
package axirassa.util;

import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;

import axirassa.dao.DAOModule;

public class TapestryTools {
	public static Registry registry = null;


	public static void init() {
		if (registry == null) {
			RegistryBuilder builder = new RegistryBuilder();
			// builder.add(HibernateModule.class);
			builder.add(DAOModule.class);

			registry = builder.build();
			registry.performRegistryStartup();
		}
	}


	public static <T> T autobuild(Class<T> classObject) {
		if (registry == null)
			init();

		return registry.autobuild(classObject);
	}
}
