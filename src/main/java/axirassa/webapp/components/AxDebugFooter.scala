package axirassa.webapp.components

import java.util.List

import org.apache.tapestry5.annotations.Property
import org.apache.tapestry5.ioc.annotations.Inject
import org.apache.tapestry5.services.Request
import org.apache.tapestry5.services.Session

class AxDebugFooter {

    @Inject
    @Property
    var request: Request 

    @Property
    var headers : List[String] = _

    @Property
    var header : String = _

    @Property
    var session : Session = _

    def setupRender () {
        session = request.getSession(false)
        headers = request.getHeaderNames()
    }


}
