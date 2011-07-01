
package axirassa.dao;

import java.util.Date;
import java.util.List;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Query;
import org.hibernate.Session;

import axirassa.model.PasswordResetTokenEntity;

public class PasswordResetTokenDAOImpl implements PasswordResetTokenDAO {

	@Inject
	private Session database;


	@Override
	public PasswordResetTokenEntity getByToken(String token) {
		Query query = database.getNamedQuery("password_reset_token");
		query.setString("token", token);
		query.setTimestamp("date", new Date());

		List<PasswordResetTokenEntity> results = query.list();
		if (results.isEmpty())
			return null;
		else
			return results.get(0);
	}


	@Override
	public int removeExpiredTokens() {
		Query query = database.getNamedQuery("remove_expired_tokens");
		query.setTimestamp("date", new Date());
		int updateCount = query.executeUpdate();
		return updateCount;
	}
}
