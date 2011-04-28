package axirassa.webapp.pages.monitor

import axirassa.model.MonitorType
import axirassa.model.PingerEntity
import org.apache.tapestry5.hibernate.annotations.CommitAfter
import java.util.Arrays
import axirassa.model.PingerFrequency
import org.apache.tapestry5.util.EnumValueEncoder
import org.apache.tapestry5.annotations.Property
import axirassa.dao.UserDAO
import axirassa.webapp.services.AxirassaSecurityService
import org.hibernate.Session
import org.apache.tapestry5.ioc.annotations.Inject
import org.apache.shiro.authz.annotation.RequiresUser

@RequiresUser
class CreateMonitor {
	@Inject
	var database : Session = _
	
	@Inject
	var security : AxirassaSecurityService =_
	
	@Inject
	var userDAO : UserDAO = _
	
	@Property
	var url : String = _
	
	@Property
	var monitorFrequency : PingerFrequency = _
	
	@CommitAfter
	def onSuccess = {
	    val pinger = new PingerEntity
	    
	    pinger.setUrl(url)
	    pinger.setFrequency(monitorFrequency.getInterval)
	    pinger.setUser(security.getUserEntity)
//	    pinger.setMonitorType(MonitorType.HTTP)
	    
	    database.save(pinger)
	    
	    classOf[ListMonitor]
	}
}