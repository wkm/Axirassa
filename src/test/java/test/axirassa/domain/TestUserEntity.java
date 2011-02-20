
package test.axirassa.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import axirassa.model.UserEntity;
import axirassa.model.exception.NoSaltException;
import axirassa.util.AbstractDomainTest;
import axirassa.util.Meta;

public class TestUserEntity extends AbstractDomainTest {
	@Test
	public void userPassword() throws NoSaltException {
		session.beginTransaction();

		UserEntity user = new UserEntity();
		user.setEMail("foo@mail.com");
		user.setSalt("tweedledee");
		user.createPassword("blah");
		session.save(user);

		assertTrue(user.matchPassword("blah"));
		assertFalse(user.matchPassword("blah "));
		assertFalse(user.matchPassword("blah123"));
		assertFalse(user.matchPassword("tweedle"));

		session.getTransaction().commit();

		Meta.inspect(user);
	}


	@Test
	public void userAutomaticSalting() throws NoSaltException {
		session.beginTransaction();
		UserEntity user = new UserEntity();
		user.setEMail("foo@bar.com");
		long start = System.currentTimeMillis();
		user.createPassword("password");
		System.out.println("PASSWORD IN: " + (System.currentTimeMillis() - start));
		session.save(user);
		session.getTransaction().commit();
	}
}
