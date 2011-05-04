
package axirassa.services

import liquibase.Liquibase
import liquibase.integration.commandline.CommandLineUtils
import liquibase.resource.ClassLoaderResourceAccessor
import org.hibernate.cfg.Configuration


class DatabaseMigrateService

object DatabaseMigrateService {
	val MASTER_CHANGELOG = "axirassa/model/changelog/db.changelog-master.xml"
		
		def main (args : Array[String]){
		val config = new Configuration().configure()
		val classloader = classOf[DatabaseMigrateService].getClassLoader()

		val target = CommandLineUtils
		        .createDatabaseObject(classloader, config.getProperty("hibernate.connection.url"),
		                              config.getProperty("hibernate.connection.username"),
		                              config.getProperty("hibernate.connection.password"),
		                              config.getProperty("hibernate.connection.driver_class"), null, null, null)

		val resourceAccessor = new ClassLoaderResourceAccessor(classloader)
		val liquibase = new Liquibase(MASTER_CHANGELOG, resourceAccessor, target)

		System.out.println("\n\n\n\n\n\n\n\n")
		System.out.println("LIQUIBASE EXECUTING DATABASE MIGRATION")
		System.out.println("=============================================")
		liquibase.update("")
	}
}