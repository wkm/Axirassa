package axirassa.webapp.pages.monitor

import axirassa.model.PingerEntity
import org.apache.tapestry5.annotations.Property
import axirassa.dao.UserDAO
import axirassa.webapp.services.AxirassaSecurityService
import org.hibernate.Session
import org.apache.tapestry5.ioc.annotations.Inject
import org.apache.shiro.authz.annotation.RequiresUser

@RequiresUser
class ListMonitor {
    @Inject
    var database : Session = _

    @Inject
    var security : AxirassaSecurityService = _

    @Inject
    var userDAO : UserDAO = _

    @Property
    var pingers : List[PingerEntity] = _

    @Property
    var pinger : PingerEntity


    def onActivate () {
        val user = security.getUserEntity;

        val query = database.createQuery("from PingerEntity where user=:user");
        query.setEntity("user", user);

        pingers = query.list.asInstanceOf[List[PingerEntity]]
    }
}