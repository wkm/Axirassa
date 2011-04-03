
package test.axirassa.domain;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.junit.Test;
import org.junit.runner.RunWith;

import axirassa.dao.PasswordResetTokenDAO;
import axirassa.ioc.IocIntegrationTestRunner;
import axirassa.model.PasswordResetTokenEntity;
import axirassa.model.UserEmailAddressEntity;
import axirassa.model.UserEntity;

@RunWith(IocIntegrationTestRunner.class)
public class TestPasswordResetTokenEntity {

	@Inject
	private PasswordResetTokenDAO passwordResetTokenDAO;

	@Inject
	private Session database;


	@Test
	public void testAutoGeneration () {
		database.beginTransaction();

		UserEntity user = new UserEntity();
		user.createPassword("password");
		database.persist(user);

		UserEmailAddressEntity emailAddress = new UserEmailAddressEntity();
		emailAddress.setEmail("who@foo.com");
		emailAddress.setUser(user);
		emailAddress.setPrimaryEmail(true);
		database.persist(emailAddress);

		PasswordResetTokenEntity token1 = new PasswordResetTokenEntity();
		token1.setUser(user);
		database.save(token1);

		PasswordResetTokenEntity token2 = new PasswordResetTokenEntity();
		token2.setUser(user);
		database.save(token2);

		String tok1 = token1.getToken();
		String tok2 = token2.getToken();

		database.getTransaction().commit();

		token1 = passwordResetTokenDAO.getByToken(tok1);
		token2 = passwordResetTokenDAO.getByToken(tok2);
	}

}
