package axirassa.webapp.pages.monitor

import axirassa.webapp.services.AxirassaSecurityException
import axirassa.model.PingerEntity
import axirassa.model.HttpStatisticsEntity
import org.apache.tapestry5.annotations.Property
import axirassa.dao.PingerDAO
import org.apache.tapestry5.services.Session
import org.apache.tapestry5.ioc.annotations.Inject
import org.apache.shiro.authz.annotation.RequiresUser

@RequiresUser
class StatisticsMonitor {
    @Inject
    var session : Session = _

    @Inject
    var pingerDAO : PingerDAO = _

    @Property
    var statistics : List[HttpStatisticsEntity] = _

    @Property
    var statistic : HttpStatisticsEntity = _

    @Property
    var pinger : PingerEntity = _

    @Property
    var id : Long = _

    def onActivate (id: Long) {
        this.id = id

        val pingerById = pingerDAO.findPingerById(id)        
        if(pingerById.isEmpty)
            throw new AxirassaSecurityException
            
        pinger = pingerById.get
        statistics = pingerDAO.findStatistics(pinger)
    }
}