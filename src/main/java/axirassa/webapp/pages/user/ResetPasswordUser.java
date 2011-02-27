
package axirassa.webapp.pages.user;

import java.io.IOException;

import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Secure;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.hibernate.Session;
import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientProducer;
import org.hornetq.api.core.client.ClientSession;

import axirassa.config.Messaging;
import axirassa.messaging.EmailRequestMessage;
import axirassa.model.PasswordResetTokenEntity;
import axirassa.model.UserEntity;
import axirassa.services.email.EmailTemplate;
import axirassa.webapp.components.AxForm;

@Secure
@RequiresGuest
public class ResetPasswordUser {

	@Inject
	private Session session;

	@Inject
	private PageRenderLinkSource linkSource;

	@Inject
	private ClientSession messagingSession;

	@Property
	private String email;

	@Component
	private AxForm form;


	public void onValidateFromForm() {
		if (email == null) {
			showInvalidEmailMessage();
			return;
		}

		UserEntity entity = UserEntity.getUserByEmail(session, email);
		if (entity == null)
			showInvalidEmailMessage();
	}


	private void showInvalidEmailMessage() {
		form.recordError("No user associated with that e-mail.");
	}


	@CommitAfter
	public Object onSuccessFromForm() throws HornetQException, IOException {
		UserEntity user = UserEntity.getUserByEmail(session, email);
		PasswordResetTokenEntity token = new PasswordResetTokenEntity();
		token.setUser(user);
		session.save(token);

		EmailRequestMessage request = new EmailRequestMessage(EmailTemplate.USER_RESET_PASSWORD);

		request.setToAddress(email);

		String link = linkSource.createPageRenderLinkWithContext(ChangePasswordByTokenUser.class, token.getToken())
		        .toAbsoluteURI(true);
		request.addAttribute("axlink", link);

		ClientProducer producer = messagingSession.createProducer(Messaging.NOTIFY_EMAIL_REQUEST);
		ClientMessage message = messagingSession.createMessage(true);
		message.getBodyBuffer().writeBytes(request.toBytes());
		producer.send(message);
		producer.close();
		messagingSession.close();

		return PasswordResetSentUser.class;
	}
}
