
package axirassa.webapp.pages.user;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;

import axirassa.model.UserEntity;
import axirassa.webapp.components.AxForm;
import axirassa.webapp.components.AxTextField;
import axirassa.webapp.services.AxirassaSecurityService;
import axirassa.webapp.services.EmailNotifyService;

@RequiresUser
public class AddEmailAddressUser {

	@Inject
	private AxirassaSecurityService security;

	@Inject
	private Session database;

	@Inject
	private EmailNotifyService emailer;

	@Component
	private AxForm form;

	@Component
	private AxTextField emailField;

	@Component
	private AxTextField emailConfirmField;

	@Property
	private String email;

	@Property
	private String emailConfirm;


	private void onValidateFromForm() {
		if (email == null)
			return;
		if (emailConfirm == null)
			return;

		if (!email.equals(emailConfirm))
			form.recordError(emailConfirmField, "e-mails do not match");
	}


	@CommitAfter
	private Object onSuccessFromForm() {
		UserEntity user = security.getUserEntity(database);

		return SettingsUser.class;
	}
}
