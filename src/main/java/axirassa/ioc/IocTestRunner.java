
package axirassa.ioc;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class IocTestRunner extends BlockJUnit4ClassRunner {

	public IocTestRunner (Class<?> classObject) throws InitializationError {
		super(classObject);
	}


	@Override
	public Object createTest () {
		return IocStandalone.autobuild(getTestClass().getJavaClass());
	}
}
