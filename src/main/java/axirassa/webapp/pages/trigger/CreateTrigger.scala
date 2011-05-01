package axirassa.webapp.pages.trigger

import org.apache.tapestry5.annotations.Property
import org.apache.shiro.authz.annotation.RequiresUser

@RequiresUser
class CreateTrigger {
  @Property
  var latency : String = _

  @Property
  var responseTime : String = _

  @Property
  var responseSize : String = _
}