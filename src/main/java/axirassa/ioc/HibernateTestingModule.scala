
package axirassa.ioc

import org.apache.tapestry5.ioc.MappedConfiguration
import org.hibernate.Session

import axirassa.util.HibernateTools

class HibernateTestingModule {
	var database : Session = _


	private def getHibernateSession () = {
		if (database == null)
			database = HibernateTools.buildTestingSessionFactory().openSession()

		database
	}


	def contributeServiceOverride (configuration :MappedConfiguration[Class[_], Object] ) {
		configuration.add(classOf[Session], getHibernateSession())
	}
}
