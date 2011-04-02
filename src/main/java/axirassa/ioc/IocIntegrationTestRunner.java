
package axirassa.ioc;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class IocIntegrationTestRunner extends BlockJUnit4ClassRunner {

	public IocIntegrationTestRunner (Class<?> classObject) throws InitializationError {
		super(classObject);
	}


	@Override
	public Object createTest () {
		Object test = IocStandalone.autobuild(getTestClass().getJavaClass());
		return test;
	}
}
