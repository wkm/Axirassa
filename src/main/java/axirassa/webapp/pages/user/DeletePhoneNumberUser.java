
package axirassa.webapp.pages.user;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;

import axirassa.model.UserPhoneNumberEntity;
import axirassa.webapp.components.AxSubmit;
import axirassa.webapp.services.AxirassaSecurityService;
import axirassa.webapp.services.exceptions.AxirassaSecurityException;

@RequiresAuthentication
public class DeletePhoneNumberUser {
	@Inject
	private Session session;

	@Inject
	private AxirassaSecurityService security;

	@Property
	private UserPhoneNumberEntity phoneNumber;

	@Component
	private AxSubmit cancelChanges;

	@Component
	private AxSubmit delete;


	public Object onActivate() {
		return SettingsUser.class;
	}


	public Object onActivate(Long phoneNumberId) throws AxirassaSecurityException {
		phoneNumber = UserPhoneNumberEntity.getByIdWithUser(session, phoneNumberId);
		security.verifyOwnership(phoneNumber);

		if (phoneNumber == null)
			return SettingsUser.class;

		return true;
	}


	public Object onPassivate() {
		return phoneNumber.getId();
	}


	public Object onSelectedFromCancelChanges() {

		return SettingsUser.class;
	}


	@CommitAfter
	public Object onSelectedFromDelete() {
		session.delete(phoneNumber);
		return SettingsUser.class;
	}
}
