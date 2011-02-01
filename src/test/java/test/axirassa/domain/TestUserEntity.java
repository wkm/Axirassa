
package test.axirassa.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import axirassa.model.UserEntity;
import axirassa.model.exception.NoSaltException;
import axirassa.util.AbstractDomainTest;

public class TestUserEntity extends AbstractDomainTest {
	@Test
	public void userPassword() throws NoSaltException {
		session.beginTransaction();

		UserEntity user = new UserEntity();
		user.setEMail("foo@mail.com");
		user.setSalt("tweedledee");
		user.createPassword("blah");
		addEntity(user);

		assertTrue(user.matchPassword("blah"));
		assertFalse(user.matchPassword("blah "));
		assertFalse(user.matchPassword("blah123"));
		assertFalse(user.matchPassword("tweedle"));

		session.getTransaction().commit();
	}


	@Test
	public void userAutomaticSalting() throws NoSaltException {
		session.beginTransaction();
		UserEntity user = new UserEntity();
		user.setEMail("foo@bar.com");
		user.createPassword("password");
		addEntity(user);
		session.getTransaction().commit();
	}
}
