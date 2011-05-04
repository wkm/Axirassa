
package axirassa.services

import liquibase.diff.Diff
import liquibase.integration.commandline.CommandLineUtils

import org.hibernate.cfg.Configuration

class DatabaseDeltaService

object DatabaseDeltaService {
    def main(args : Array[String]) {
        // pull the database configuration from hibernate
        val config = new Configuration().configure()

        val classloader = classOf[DatabaseDeltaService].getClassLoader()
        val target = CommandLineUtils
            .createDatabaseObject(classloader, config.getProperty("hibernate.connection.url"),
                config.getProperty("hibernate.connection.username"),
                config.getProperty("hibernate.connection.password"),
                config.getProperty("hibernate.connection.driver_class"), null, null, null)

        val reference = CommandLineUtils.createDatabaseObject(classloader, "hibernate:hibernate.cfg.xml", null,
            null, null, null, null, null)

        val diff = new Diff(reference, target)
        val result = diff.compare()

        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n")
        System.out.println("LIQUIBASE DATABASE DELTA:")
        System.out.println("=============================================")
        result.printChangeLog(System.out, target)
    }
}
