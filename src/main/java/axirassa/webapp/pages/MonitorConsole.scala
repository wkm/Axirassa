package axirassa.webapp.pages

import axirassa.model.PingerEntity
import org.apache.tapestry5.annotations.Property
import axirassa.webapp.services.AxirassaSecurityService
import axirassa.dao.UserDAO
import org.hibernate.Session
import org.apache.tapestry5.ioc.annotations.Inject
import org.apache.shiro.authz.annotation.RequiresUser


class MonitorConsole {
    
	@Inject
	var session : Session = _

	@Inject
	var security : AxirassaSecurityService = _

	@Inject
	var userDAO : UserDAO = _

	@Property
	var pingers : List[PingerEntity] = _

	@Property
	var pinger : PingerEntity = _


	def onActivate () {
		val user = security.getUserEntity

		val  query = session.createQuery("from PingerEntity where user=:user")
		// query.setMaxResults(1)
		query.setEntity("user", user)

		pingers = query.list().asInstanceOf[List[PingerEntity]]
	}
}