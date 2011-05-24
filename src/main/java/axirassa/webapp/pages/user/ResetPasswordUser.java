
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

import axirassa.dao.UserEmailAddressDAO;
import axirassa.model.PasswordResetTokenEntity;
import axirassa.model.UserEntity;
import axirassa.services.email.EmailTemplate;
import axirassa.webapp.components.AxForm;
import axirassa.webapp.services.EmailNotifyService;

@Secure
@RequiresGuest
public class ResetPasswordUser {

	@Inject
	private Session database;

	@Inject
	private UserEmailAddressDAO emailDAO;

	@Inject
	private PageRenderLinkSource linkSource;

	@Inject
	private EmailNotifyService emailNotify;

	@Property
	private String email;

	@Component
	private AxForm form;


	public void onValidateFromForm() {
		if (email == null) {
			showInvalidEmailMessage();
			return;
		}

		UserEntity entity = emailDAO.getUserByEmail(email);
		if (entity == null)
			showInvalidEmailMessage();
	}


	private void showInvalidEmailMessage() {
		form.recordError("No user associated with that e-mail.");
	}


	@CommitAfter
	public Object onSuccessFromForm() throws HornetQException, IOException {
		UserEntity user = emailDAO.getUserByEmail(email);
		PasswordResetTokenEntity token = new PasswordResetTokenEntity();
		token.setUser(user);
		database.save(token);

		String link = linkSource.createPageRenderLinkWithContext(ChangePasswordByTokenUser.class, token.getToken())
		        .toAbsoluteURI(true);

		emailNotify.startMessage(EmailTemplate.USER_RESET_PASSWORD);
		emailNotify.setToAddress(email);
		emailNotify.addAttribute("axlink", link);
		emailNotify.send();

		return PasswordResetSentUser.class;
	}
}
