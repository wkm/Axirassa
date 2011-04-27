package axirassa.ioc

import org.apache.tapestry5.ioc.Registry
import org.apache.tapestry5.ioc.RegistryBuilder

class IocStandalone {

    var registry : Registry = _

    def initRegistryBuilder() = {
        val builder = new RegistryBuilder()
        builder.add(classOf[DAOModule])
        builder.add(classOf[FlowsModule])
        builder.add(classOf[MessagingModule])
        builder.add(classOf[LoggingModule])

        classOf[IocStandalone]

        builder
    }

    def init() {
        init(initRegistryBuilder())
    }

    def init(builder : RegistryBuilder) {
        if (registry == null) {
            registry = builder.build()
            registry.performRegistryStartup()
        }
    }

    def autobuild[T](classObject : Class[T]) = {
        if (registry == null)
            init()

        registry.autobuild(classObject)
    }
}

object IocStandalone extends IocStandalone
