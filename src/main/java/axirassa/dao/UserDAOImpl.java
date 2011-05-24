
package axirassa.dao;

import java.util.List;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Query;
import org.hibernate.Session;

import axirassa.model.UserEntity;

public class UserDAOImpl implements UserDAO {
	@Inject
	private Session database;

	@Inject
	private UserEmailAddressDAO emailAddressDAO;


	@Override
	@Deprecated
	public boolean isEmailRegistered(String email) {
		return emailAddressDAO.isEmailRegistered(email);
	}


	@Override
	public UserEntity getUserByEmail(String email) {
		Query query = database.getNamedQuery("user_by_email");
		query.setString("email", email.toLowerCase());

		List<UserEntity> users = query.list();

		if (users.size() <= 0)
			return null;

		return users.iterator().next();
	}
}
