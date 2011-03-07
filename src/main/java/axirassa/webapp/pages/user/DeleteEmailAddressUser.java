
package axirassa.webapp.pages.user;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;

import axirassa.model.UserEmailAddressEntity;
import axirassa.webapp.components.AxSubmit;
import axirassa.webapp.services.AxirassaSecurityService;
import axirassa.webapp.services.exceptions.AxirassaSecurityException;

@RequiresAuthentication
public class DeleteEmailAddressUser {
	@Inject
	private Session database;

	@Inject
	private AxirassaSecurityService security;

	@Property
	private UserEmailAddressEntity email;

	@Component
	private AxSubmit cancelChanges;

	@Component
	private AxSubmit delete;


	public Object onActivate() {
		return SettingsUser.class;
	}


	public Object onActivate(Long emailId) throws AxirassaSecurityException {
		email = UserEmailAddressEntity.getByIdWithUser(database, emailId);
		security.verifyOwnership(email);

		if (email == null)
			return SettingsUser.class;

		return true;
	}


	public Object onPassivate() {
		return email.getId();
	}


	public Object onSelectedFromCancelChanges() {
		return SettingsUser.class;
	}


	@CommitAfter
	public Object onSelectedFromDelete() {
		database.delete(email);
		return SettingsUser.class;
	}
}
