package axirassa.ioc

import org.apache.tapestry5.ioc.Registry
import org.junit.runners.BlockJUnit4ClassRunner
import org.mockito.MockitoAnnotations

/**
 *
 * @author wiktor
 */
class IocUnitTestRunner(classObject:Class[_]) extends BlockJUnit4ClassRunner(classObject) {

    private var registry: Registry = _

    val builder = IocStandalone.initRegistryBuilder()
    builder.add(classOf[MockingModule])

    registry = builder.build()
    registry.performRegistryStartup()


    override
    def createTest () = {
        val test = registry.autobuild(getTestClass().getJavaClass())
        MockitoAnnotations.initMocks(test)

        test.asInstanceOf[Object]
    }


}
