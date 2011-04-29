
package axirassa.services

import java.util.List

import org.apache.tapestry5.hibernate.annotations.CommitAfter
import org.apache.tapestry5.ioc.annotations.Inject
import org.hibernate.Session

import axirassa.dao.FeedbackDAO
import axirassa.ioc.IocStandalone
import axirassa.model.FeedbackEntity
import axirassa.services.email.EmailTemplate
import axirassa.webapp.services.EmailNotifyService
import axirassa.webapp.services.MessagingSession

object FeedbackAggregationService {
    def main(args : Array[String]) {
        val service = IocStandalone.autobuild(classOf[FeedbackAggregationService])
        service.execute()
    }
}

class FeedbackAggregationService extends Service {

    @Inject
    var feedbackDAO : FeedbackDAO = _

    @Inject
    var session : Session = _

    @Inject
    var notifyService : EmailNotifyService = _

    @Inject
    var messagingSession : MessagingSession = _

    @CommitAfter
    override def execute() {
        // get feedback
        val feedback = feedbackDAO.getAllFeedback

        if (feedback.size > 0) {
            // try to send it
            notifyService.startMessage(EmailTemplate.AGGREGATED_FEEDBACK)
            notifyService.setToAddress("feedback@axirassa.com")
            notifyService.addAttribute("feedback", feedback)
            notifyService.addAttribute("feedbackCount", feedback.size.asInstanceOf[AnyRef])
            notifyService.send()

            // delete feedback
            for (entity <- feedback) {
                entity.setPosted(true)
                session.update(entity)
            }
        }
    }
}
