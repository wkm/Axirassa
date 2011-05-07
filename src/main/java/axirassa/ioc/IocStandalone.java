
package axirassa.ioc;

import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;

public class IocStandalone {

	public static Registry registry = null;


	public static RegistryBuilder initRegistryBuilder() {
		RegistryBuilder builder = new RegistryBuilder();
		builder.add(DAOModule.class);
		builder.add(FlowsModule.class);
		builder.add(MessagingModule.class);
		builder.add(LoggingModule.class);

		return builder;
	}


	public static void init() {
		init(initRegistryBuilder());
	}


	public static void init(RegistryBuilder builder) {
		if (registry == null) {
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
