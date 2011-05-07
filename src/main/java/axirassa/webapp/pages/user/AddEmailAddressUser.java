
package axirassa.webapp.pages.user;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.hibernate.Session;

import axirassa.model.UserEntity;
import axirassa.model.flows.AddEmailAddressFlow;
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

	@Inject
	private AddEmailAddressFlow addEmailFlow;


	public void onValidateFromForm() {
		if (email == null)
			return;
		if (emailConfirm == null)
			return;

		if (!email.equals(emailConfirm))
			form.recordError(emailConfirmField, "e-mails do not match");
	}


	@CommitAfter
	public Object onSuccessFromForm() throws Exception {
		UserEntity user = security.getUserEntity();

		addEmailFlow.setUserEntity(security.getUserEntity());
		addEmailFlow.setEmailAddress(email);
		addEmailFlow.execute();

		return SettingsUser.class;
	}
}
