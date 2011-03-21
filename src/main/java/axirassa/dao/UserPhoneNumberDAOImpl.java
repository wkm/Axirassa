
package axirassa.dao;

import java.util.List;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Query;
import org.hibernate.Session;

import axirassa.model.UserEntity;
import axirassa.model.UserPhoneNumberEntity;

public class UserPhoneNumberDAOImpl {
	@Inject
	private Session database;


	public List<UserPhoneNumberEntity> getPhoneNumbersByUser(UserEntity user) {
		Query query = database.getNamedQuery("user_phonenumbers");
		query.setEntity("user", user);

		return query.list();
	}


	public UserPhoneNumberEntity getByIdWithUser(long id) {
		Query query = database.getNamedQuery("phonenumber_by_id");
		query.setLong("id", id);

		List<UserPhoneNumberEntity> list = query.list();
		if (list.size() == 0)
			return null;
		else
			return list.iterator().next();
	}
}
