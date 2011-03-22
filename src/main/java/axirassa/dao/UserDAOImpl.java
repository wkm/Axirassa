package axirassa.dao;


import axirassa.model.UserEntity;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;


public class UserDAOImpl implements UserDAO {
	@Inject
	private Session database;


	@Override
	public boolean isEmailRegistered (String email) {
		Query query = database.getNamedQuery("user_is_email_registered");
		query.setString("email", email);

		List results = query.list();
		boolean isregistered = false;
		if (results.size() > 0)
			return true;

		return isregistered;
	}


	@Override
	public UserEntity getUserByEmail (String email) {
		Query query = database.getNamedQuery("user_by_email");
		query.setString("email", email);

		List<UserEntity> users = query.list();

		if (users.size() <= 0)
			return null;

		return users.iterator().next();
	}
}
