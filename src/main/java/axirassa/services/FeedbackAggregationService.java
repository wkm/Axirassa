
package axirassa.services;

import java.util.List;

import org.hibernate.Session;
import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.client.ClientSession;

import axirassa.model.FeedbackEntity;
import axirassa.services.email.EmailTemplate;
import axirassa.webapp.services.EmailNotifyService;
import axirassa.webapp.services.MessagingSession;
import axirassa.webapp.services.internal.EmailNotifyServiceImpl;
import axirassa.webapp.services.internal.MessagingSessionImpl;

public class FeedbackAggregationService implements Service {
	private final Session session;
	private final EmailNotifyService notifyService;
	private final MessagingSession messagingSession;


	public FeedbackAggregationService(Session session, ClientSession messaging) throws HornetQException {
		this.session = session;
		this.messagingSession = new MessagingSessionImpl(messaging);
		this.notifyService = new EmailNotifyServiceImpl(messagingSession);
	}


	@Override
	public void execute() throws Exception {
		session.beginTransaction();

		// get feedback
		List<FeedbackEntity> feedback = FeedbackEntity.getAllFeedback(session);

		// try to send it
		notifyService.startMessage(EmailTemplate.AGGREGATED_FEEDBACK);
		notifyService.setToAddress("feedback@axirassa.com");
		notifyService.addAttribute("feedback", feedback);
		notifyService.addAttribute("feedbackCount", feedback.size());
		notifyService.send();

		// delete feedback
		// TODO

		session.getTransaction().commit();
		messagingSession.close();
	}
}
