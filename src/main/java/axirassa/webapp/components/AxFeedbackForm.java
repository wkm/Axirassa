
package axirassa.webapp.components;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.hibernate.Session;

import axirassa.model.FeedbackEntity;
import axirassa.model.UserEntity;
import axirassa.webapp.services.AxirassaSecurityService;
import axirassa.webapp.services.exceptions.AxirassaSecurityException;

public class AxFeedbackForm {
	@Inject
	private Request request;

	@Inject
	private Session session;

	@Inject
	private AxirassaSecurityService security;

	@Component
	private TextField feedbackField;

	@Component
	private Form feedbackForm;

	@Property
	private String feedback;


	@CommitAfter
	public Object onSuccessFromFeedbackForm() throws AxirassaSecurityException {
		System.err.println("Hi from action");
		FeedbackEntity feedbackEntity = new FeedbackEntity();
		feedbackEntity.setComment(feedback);

		if (security.isUser()) {
			UserEntity user = security.getUserEntity();
			feedbackEntity.setUser(user);
		}

		feedbackEntity.setUseragent(request.getHeader("User-Agent"));
		session.save(feedbackEntity);

		return true;
	}
}
