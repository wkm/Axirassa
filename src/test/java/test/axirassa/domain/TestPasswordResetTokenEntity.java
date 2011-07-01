
package test.axirassa.domain;

import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.junit.Test;

import axirassa.dao.PasswordResetTokenDAO;
import axirassa.model.PasswordResetTokenEntity;
import axirassa.model.UserEmailAddressEntity;
import axirassa.model.UserEntity;
import axirassa.util.test.AbstractIntegrationTest;

public class TestPasswordResetTokenEntity extends AbstractIntegrationTest {

	@Inject
	private PasswordResetTokenDAO passwordResetTokenDAO;

	@Inject
	private Session database;


	@Test
	@CommitAfter
	public void testAutoGeneration() {
		UserEntity user = new UserEntity();
		user.createPassword("password");
		database.save(user);

		UserEmailAddressEntity emailAddress = new UserEmailAddressEntity();
		emailAddress.setEmail("who@foo.com");
		emailAddress.setUser(user);
		emailAddress.setPrimaryEmail(true);
		database.save(emailAddress);

		PasswordResetTokenEntity token1 = new PasswordResetTokenEntity();
		token1.setUser(user);
		database.save(token1);

		PasswordResetTokenEntity token2 = new PasswordResetTokenEntity();
		token2.setUser(user);
		database.save(token2);

		String tok1 = token1.getToken();
		String tok2 = token2.getToken();

		token1 = passwordResetTokenDAO.getByToken(tok1);
		token2 = passwordResetTokenDAO.getByToken(tok2);
	}

}
