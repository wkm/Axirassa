
package axirassa.webapp.components

import org.apache.tapestry5.annotations.Component
import org.apache.tapestry5.annotations.Property
import org.apache.tapestry5.corelib.components.Form
import org.apache.tapestry5.corelib.components.TextField
import org.apache.tapestry5.hibernate.annotations.CommitAfter
import org.apache.tapestry5.ioc.annotations.Inject
import org.apache.tapestry5.services.Request
import org.hibernate.Session

import axirassa.dao.UserDAO
import axirassa.model.FeedbackEntity
import axirassa.model.UserEntity
import axirassa.webapp.services.AxirassaSecurityService
import axirassa.webapp.services.AxirassaSecurityException

class AxFeedbackForm {
  @Inject
  var request : Request = _

  @Inject
  var session : Session = _

  @Inject
  var userDAO : UserDAO = _

  @Inject
  var security : AxirassaSecurityService = _

  @Component
  var feedbackField : TextField = _

  @Component
  var feedbackForm : Form = _

  @Property
  var feedback : String = _

  @CommitAfter
  def onSuccessFromFeedbackForm() = {
    val feedbackEntity = new FeedbackEntity()
    feedbackEntity.setComment(feedback)

    if (security.isUser) {
      val user = security.getUserEntity
      feedbackEntity.setUser(user)
    }

    feedbackEntity.setUseragent(request.getHeader("User-Agent"))
    session.save(feedbackEntity)

    true
  }
}
