package axirassa.ioc;

import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.mockito.MockitoAnnotations;

/**
 *
 * @author wiktor
 */
public class IocUnitTestRunner extends BlockJUnit4ClassRunner {

    private final Registry registry;

    public IocUnitTestRunner (Class<?> classObject) throws InitializationError {
        super(classObject);

        RegistryBuilder builder = IocStandalone.initRegistryBuilder();
        builder.add(MockingModule.class);

        registry = builder.build();
        registry.performRegistryStartup();
    }


    @Override
    public Object createTest () {
        Object test = registry.autobuild(getTestClass().getJavaClass());
        MockitoAnnotations.initMocks(test);

        return test;
    }


}
