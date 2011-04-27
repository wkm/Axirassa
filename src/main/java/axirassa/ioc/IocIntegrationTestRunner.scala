
package axirassa.ioc

import org.apache.tapestry5.ioc.Registry
import org.apache.tapestry5.ioc.RegistryBuilder
import org.junit.runners.BlockJUnit4ClassRunner
import org.junit.runners.model.InitializationError

class IocIntegrationTestRunner(classObject : Class[_]) extends BlockJUnit4ClassRunner(classObject) {

    var registry : Registry = _
    val builder = IocStandalone.initRegistryBuilder()
    
    builder.add(classOf[HibernateTestingModule])

    registry = builder.build()
    registry.performRegistryStartup()

    override def createTest() = {
        registry.autobuild(getTestClass().getJavaClass()).asInstanceOf[Object]
    }
}
