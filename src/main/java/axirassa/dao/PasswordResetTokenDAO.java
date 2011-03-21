
package axirassa.dao;

import axirassa.model.PasswordResetTokenEntity;

public interface PasswordResetTokenDAO {
	public PasswordResetTokenEntity getByToken(String token);


	public int removeExpiredTokens();
}
