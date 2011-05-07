
package axirassa.ioc;

import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class IocIntegrationTestRunner extends BlockJUnit4ClassRunner {

	private final Registry registry;


	public IocIntegrationTestRunner(Class<?> classObject) throws InitializationError {
		super(classObject);

		RegistryBuilder builder = IocStandalone.initRegistryBuilder();
		builder.add(HibernateTestingModule.class);

		registry = builder.build();
		registry.performRegistryStartup();
	}


	@Override
	public Object createTest() {
		Object test = registry.autobuild(getTestClass().getJavaClass());
		return test;
	}

}
