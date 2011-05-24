
package axirassa.dao;

import java.util.List;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Query;
import org.hibernate.Session;

import axirassa.model.UserEmailAddressEntity;
import axirassa.model.UserEntity;

public class UserEmailAddressDAOImpl implements UserEmailAddressDAO {

	@Inject
	private Session database;


	@Override
	public boolean isEmailRegistered(String email) {
		Query query = database.getNamedQuery("get_email");
		query.setString("email", email);

		List<UserEmailAddressEntity> results = query.list();
		if (results.size() > 0)
			return true;
		else
			return false;
	}


	@Override
	public UserEntity getUserByEmail(String email) {
		Query query = database.getNamedQuery("user_by_email");
		query.setString("email", email);

		List<UserEntity> emails = query.list();
		if (emails.size() > 0)
			return emails.iterator().next();
		else
			return null;
	}


	@Override
	public UserEmailAddressEntity getByIdWithUser(long id) {
		Query query = database.getNamedQuery("email_by_id");
		query.setLong("id", id);

		List<UserEmailAddressEntity> emails = query.list();
		if (emails.size() > 0)
			return emails.iterator().next();
		else
			return null;
	}


	@Override
	public List<UserEmailAddressEntity> getEmailsByUser(UserEntity user) {
		Query query = database.getNamedQuery("user_emails");
		query.setEntity("user", user);
		return query.list();
	}


	@Override
	public UserEmailAddressEntity getPrimaryEmail(UserEntity user) {
		Query query = database.getNamedQuery("user_primary_email");
		query.setEntity("user", user);

		List<UserEmailAddressEntity> emails = query.list();
		if (emails.size() > 0)
			return emails.iterator().next();
		else
			return null;
	}


	@Override
	public UserEmailAddressEntity getByToken(String token) {
		Query query = database.getNamedQuery("email_by_token");
		query.setString("token", token);

		List<UserEmailAddressEntity> email = query.list();
		if (email.isEmpty())
			return null;
		else
			return email.iterator().next();
	}


	@Override
	public UserEmailAddressEntity getByEmail(String email) {
		Query query = database.getNamedQuery("email_by_email");
		query.setString("email", email);

		List<UserEmailAddressEntity> emailEntity = query.list();
		if (emailEntity.isEmpty())
			return null;
		else
			return emailEntity.iterator().next();
	}

}
