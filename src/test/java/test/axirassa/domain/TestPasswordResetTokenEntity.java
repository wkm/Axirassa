
package test.axirassa.domain;

import org.junit.Test;

import axirassa.model.PasswordResetTokenEntity;
import axirassa.model.UserEntity;
import axirassa.util.AbstractDomainTest;

public class TestPasswordResetTokenEntity extends AbstractDomainTest {
	@Test
	public void testAutoGeneration() {
		session.beginTransaction();
		UserEntity user = new UserEntity();
		user.setEMail("who@foo.com");
		user.createPassword("password");
		addEntity(user);

		PasswordResetTokenEntity token1 = new PasswordResetTokenEntity();
		token1.setUser(user);
		addEntity(token1);

		PasswordResetTokenEntity token2 = new PasswordResetTokenEntity();
		token2.setUser(user);
		addEntity(token2);

		session.getTransaction().commit();
	}
}
