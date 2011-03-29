
package test.axirassa.domain;

import org.junit.Test;
import org.junit.runner.RunWith;

import axirassa.ioc.IocTestRunner;
import axirassa.model.PasswordResetTokenEntity;
import axirassa.model.UserEntity;
import axirassa.util.AbstractDomainTest;

@RunWith(IocTestRunner.class)
public class TestPasswordResetTokenEntity extends AbstractDomainTest {
	@Test
	public void testAutoGeneration () {
		session.beginTransaction();
		UserEntity user = new UserEntity();
		user.setEmail("who@foo.com");
		user.createPassword("password");
		session.save(user);

		PasswordResetTokenEntity token1 = new PasswordResetTokenEntity();
		token1.setUser(user);
		session.save(token1);

		PasswordResetTokenEntity token2 = new PasswordResetTokenEntity();
		token2.setUser(user);
		session.save(token2);

		String tok1 = token1.getToken();
		String tok2 = token2.getToken();

		session.getTransaction().commit();

		token1 = PasswordResetTokenEntity.getByToken(session, tok1);
		token2 = PasswordResetTokenEntity.getByToken(session, tok2);
	}
}
