package axirassa.webapp.pages.user;


import axirassa.dao.UserEmailAddressDAO;
import axirassa.model.UserEmailAddressEntity;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;


public class VerifyEmailUser {

	@Inject
	private Session database;

	@Inject
	private UserEmailAddressDAO userEmailAddressDAO;

	@Property
	private boolean isTokenInvalid;


	public Object onActivate () {
		return SettingsUser.class;
	}


	@CommitAfter
	public Object onActivate (String token) {
		UserEmailAddressEntity emailAddress = userEmailAddressDAO.getByToken(token);

		if (emailAddress == null) {
			isTokenInvalid = true;
			return true;
		}

		emailAddress.setVerified(true);
		database.save(emailAddress);

		return SettingsUser.class;

	}
}
