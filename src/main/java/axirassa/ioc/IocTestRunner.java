
package axirassa.ioc;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.mockito.MockitoAnnotations;

public class IocTestRunner extends BlockJUnit4ClassRunner {

	public IocTestRunner (Class<?> classObject) throws InitializationError {
		super(classObject);
	}


	@Override
	public Object createTest () {
		Object test = IocStandalone.autobuild(getTestClass().getJavaClass());
		MockitoAnnotations.initMocks(test);

		return test;
	}
}
