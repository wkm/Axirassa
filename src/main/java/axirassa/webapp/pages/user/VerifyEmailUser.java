
package axirassa.webapp.pages.user;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;

import axirassa.model.UserEmailAddressEntity;

public class VerifyEmailUser {

	@Inject
	private Session database;

	@Property
	private boolean isTokenInvalid;


	public Object onActivate() {
		return SettingsUser.class;
	}


	@CommitAfter
	public Object onActivate(String token) {
		UserEmailAddressEntity emailAddress = UserEmailAddressEntity.getByToken(database, token);

		if (emailAddress == null) {
			isTokenInvalid = true;
			return true;
		}

		emailAddress.setVerified(true);
		database.save(emailAddress);

		return SettingsUser.class;

	}
}
