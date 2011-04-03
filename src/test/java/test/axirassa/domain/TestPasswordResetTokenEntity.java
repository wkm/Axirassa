
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
import axirassa.model.flows.CreateUserFlow;

@RunWith(IocIntegrationTestRunner.class)
public class TestPasswordResetTokenEntity {

	@Inject
	private PasswordResetTokenDAO passwordResetTokenDAO;

	@Inject
	private CreateUserFlow createUserFlow;

	@Inject
	private Session database;


	@Test
	public void testAutoGeneration () {
		database.beginTransaction();

		createUserFlow.setEmail("who@foo.com");
		createUserFlow.setPassword("password");
		createUserFlow.execute();

		UserEntity user = createUserFlow.getUserEntity();
		UserEmailAddressEntity emailAddress = createUserFlow.getPrimaryEmailEntity();

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
