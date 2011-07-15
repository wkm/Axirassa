
package axirassa.services;

import java.util.List;

import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;

import axirassa.dao.FeedbackDAO;
import axirassa.ioc.IocStandalone;
import axirassa.model.FeedbackEntity;
import axirassa.services.email.EmailTemplate;
import axirassa.webapp.services.EmailNotifyService;
import axirassa.webapp.services.MessagingSession;

public class FeedbackAggregationService implements Service {

	@Inject
	private FeedbackDAO feedbackDAO;

	@Inject
	private Session session;

	@Inject
	private EmailNotifyService notifyService;

	@Inject
	private MessagingSession messagingSession;


	@Override
	@CommitAfter
	public void execute() throws Exception {
		// get feedback
		List<FeedbackEntity> feedback = feedbackDAO.getAllFeedback();

		if (!feedback.isEmpty()) {
			// try to send it
			notifyService.startMessage(EmailTemplate.AGGREGATED_FEEDBACK);
			notifyService.setToAddress("feedback@axirassa.com");
			notifyService.addAttribute("feedback", feedback);
			notifyService.addAttribute("feedbackCount", feedback.size());
			notifyService.send();

			// delete feedback
			for (FeedbackEntity entity : feedback) {
				entity.setPosted(true);
				session.update(entity);
			}
		}
	}


	public static void main(String[] args) throws Throwable {
		Service service = IocStandalone.autobuild(FeedbackAggregationService.class);
		service.execute();
	}
}
