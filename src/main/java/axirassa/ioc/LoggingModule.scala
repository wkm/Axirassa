
package axirassa.ioc;


import org.slf4j.LoggerFactory;

/**
 *
 * @author wiktor
 */
class LoggingModule {
    def buildLogger() = LoggerFactory.getLogger(classOf[LoggingModule])
}
