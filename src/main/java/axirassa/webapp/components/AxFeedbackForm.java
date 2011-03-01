
package axirassa.webapp.components;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.hibernate.Session;
import org.tynamo.security.services.SecurityService;

import axirassa.model.FeedbackEntity;
import axirassa.model.UserEntity;

public class AxFeedbackForm {
	@Inject
	private Request request;

	@Inject
	private Session session;

	@Inject
	private SecurityService security;

	@Component
	private TextField feedbackField;

	@Component
	private Form feedbackForm;

	@Property
	private String feedback;


	@CommitAfter
	public Object onSuccessFromFeedbackForm() {
		System.err.println("Hi from action");
		FeedbackEntity feedbackEntity = new FeedbackEntity();
		feedbackEntity.setComment(feedback);

		if (security.isUser()) {
			String email = (String) security.getSubject().getPrincipal();
			UserEntity user = UserEntity.getUserByEmail(session, email);
			feedbackEntity.setUser(user);
		}

		feedbackEntity.setUseragent(request.getHeader("User-Agent"));
		session.save(feedbackEntity);

		return true;
	}
}
