
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
	@Deprecated
	public UserEntity getUserByEmail(String email) {
		return emailAddressDAO.getUserByEmail(email);
	}
}
