
package axirassa.webapp.pages.user;

import java.io.IOException;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.hibernate.Session;
import org.hornetq.api.core.HornetQException;

import axirassa.model.UserEmailAddressEntity;
import axirassa.model.UserEntity;
import axirassa.services.email.EmailTemplate;
import axirassa.webapp.components.AxForm;
import axirassa.webapp.components.AxTextField;
import axirassa.webapp.services.AxirassaSecurityService;
import axirassa.webapp.services.EmailNotifyService;

@RequiresUser
public class AddEmailAddressUser {

	@Inject
	private PageRenderLinkSource linkSource;

	@Inject
	private AxirassaSecurityService security;

	@Inject
	private Session database;

	@Inject
	private EmailNotifyService emailer;

	@Component
	private AxForm form;

	@Component
	private AxTextField emailConfirmField;

	@Property
	private String email;
    
	@Property
	private String emailConfirm;


	public void onValidateFromForm() {
		if (email == null)
			return;
		if (emailConfirm == null)
			return;

		if (!email.equals(emailConfirm))
			form.recordError(emailConfirmField, "e-mails do not match");
	}


	@CommitAfter
	public Object onSuccessFromForm() throws HornetQException, IOException {
		UserEntity user = security.getUserEntity();

		UserEmailAddressEntity emailEntity = new UserEmailAddressEntity();
		emailEntity.setUser(user);
		emailEntity.setEmail(email);
		database.save(emailEntity);

		String link = linkSource.createPageRenderLinkWithContext(VerifyEmailUser.class, emailEntity.getToken())
		        .toAbsoluteURI(true);

		emailer.startMessage(EmailTemplate.USER_VERIFY_ACCOUNT);
		emailer.setToAddress(email);
		emailer.addAttribute("axlink", link);
		emailer.send();

		return SettingsUser.class;
	}
}
