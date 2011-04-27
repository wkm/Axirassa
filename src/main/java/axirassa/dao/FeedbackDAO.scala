package axirassa.dao

import org.hibernate.Session
import org.apache.tapestry5.ioc.annotations.Inject
import axirassa.model.FeedbackEntity

trait FeedbackDAO {
    def getAllFeedback : List[FeedbackEntity]
}

class FeedbackDAOImpl extends FeedbackDAO {
    @Inject
    var database : Session = _
    
    override
    def getAllFeedback = {
        database.getNamedQuery("unposted_feedback_with_users").list.asInstanceOf[List[FeedbackEntity]]
    }
}