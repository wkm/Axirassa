
package axirassa.model.flows;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.hibernate.Session;

import axirassa.dao.UserEmailAddressDAO;
import axirassa.model.PasswordResetTokenEntity;
import axirassa.model.UserEntity;
import axirassa.services.email.EmailTemplate;
import axirassa.webapp.pages.user.ChangePasswordByTokenUser;
import axirassa.webapp.services.EmailNotifyService;

public class ResetPasswordFlowImpl implements ResetPasswordFlow {

	@Inject
	private Session database;

	@Inject
	private PageRenderLinkSource linkSource;

	@Inject
	private EmailNotifyService emailNotify;

	@Inject
	private UserEmailAddressDAO emailDAO;

	private UserEntity user;


	@Override
	public void setUserEntity(UserEntity user) {
		this.user = user;
	}


	@Override
	public void execute() throws Exception {
		PasswordResetTokenEntity token = new PasswordResetTokenEntity();
		token.setUser(user);
		database.save(token);

		String email = emailDAO.getPrimaryEmail(user).getEmail();

		String link = linkSource.createPageRenderLinkWithContext(ChangePasswordByTokenUser.class, token.getToken())
		        .toAbsoluteURI(true);

		emailNotify.startMessage(EmailTemplate.USER_RESET_PASSWORD);
		emailNotify.setToAddress(email);
		emailNotify.addAttribute("axlink", link);
		emailNotify.send();
	}
}
