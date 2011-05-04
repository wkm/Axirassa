
package axirassa.util

import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration

object HibernateTools {
  var sessionfactory : SessionFactory = _

  def buildSessionFactory() = {
    try {
      val config = new Configuration().configure()
      config.setProperty("hibernate.c3p0.min_size", "1")
      config.setProperty("hibernate.c3p0.max_Size", "1")

      config.buildSessionFactory()

    } catch {
      case ex => {
        System.err.println("Initial SessionFactory creation failed."+ex)
        throw new ExceptionInInitializerError(ex)
      }
    }
  }

  /**
   * builds and returns a {@link SessionFactory} connected to the
   * <tt>axir_test</tt> schema and set to drop and create the database schema
   * on connect.
   */
  def buildTestingSessionFactory() = {
    val config = new Configuration()
    config.configure()
    config.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost/axir_test")
    config.setProperty("hibernate.hbm2ddl.auto", "create-drop")
    config.setProperty("hibernate.show_sql", "true")

    // disable c3p0
    config.setProperty("hibernate.c3p0.min_size", "")
    config.setProperty("hibernate.c3p0.max_size", "")
    config.setProperty("hibernate.c3p0.timeout", "")
    config.setProperty("hibernate.c3p0.max_statements", "")

    config.buildSessionFactory()
  }

  def getSessionFactory() = {
    if (sessionfactory == null)
      sessionfactory = buildSessionFactory()
    sessionfactory
  }

  def setSessionFactory(factory : SessionFactory) {
    sessionfactory = factory
  }

  def getLightweightSession() = getSessionFactory().openSession()
}
