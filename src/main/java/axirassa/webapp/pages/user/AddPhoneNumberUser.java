
package axirassa.webapp.pages.user;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Secure;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.hibernate.Session;

import axirassa.dao.UserDAO;
import axirassa.model.UserEntity;
import axirassa.model.UserPhoneNumberEntity;
import axirassa.webapp.components.AxCheckbox;
import axirassa.webapp.components.AxForm;
import axirassa.webapp.services.AxirassaSecurityService;
import axirassa.webapp.services.exceptions.AxirassaSecurityException;

@Secure
@RequiresUser
public class AddPhoneNumberUser {
	@Inject
	private AxirassaSecurityService security;

	@Inject
	private Session database;

	@Inject
	private UserDAO userDAO;

	@Inject
	private PageRenderLinkSource linkSource;

	@Property
	@Validate("required,regexp=[-+*#0-9a-zA-Z.\\, ()]+,minlength=10")
	private String phoneNumber;

	@Property
	@Validate("regexp=[-*#0-9]+")
	private String extension;

	@Property
	private boolean acceptsVoice;

	@Property
	private boolean acceptsText;

	@Component
	private AxCheckbox acceptsTextField;

	@Component
	private AxCheckbox acceptsVoiceField;

	@Component
	private AxForm form;

	private String token;


	public void onValidateFromForm() {
		if (extension != null && acceptsText == true)
			form.recordError(acceptsTextField, "Text messages may not be sent to phone numbers with extensions");

		if (!acceptsText && !acceptsVoice)
			form.recordError("Please specify your notification method preference");
	}


	@CommitAfter
	public Object onSuccess() throws AxirassaSecurityException {
		UserEntity user = security.getUserEntity();

		UserPhoneNumberEntity phoneNumberEntity = new UserPhoneNumberEntity();
		phoneNumberEntity.setUser(user);
		phoneNumberEntity.setPhoneNumber(phoneNumber);
		phoneNumberEntity.setExtension(extension);
		phoneNumberEntity.setAcceptingSms(acceptsText);
		phoneNumberEntity.setAcceptingVoice(acceptsVoice);
		phoneNumberEntity.setConfirmed(false);
		database.save(phoneNumberEntity);

		Link link = linkSource.createPageRenderLinkWithContext(VerifyPhoneNumberUser.class, phoneNumberEntity.getId());
		return link;
	}
}
