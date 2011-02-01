
package test.axirassa.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import axirassa.model.UserEntity;
import axirassa.model.exception.NoSaltException;
import axirassa.util.AbstractDomainTest;

public class TestUserModel extends AbstractDomainTest {
	@Test
	public void userPassword() throws NoSaltException {
		session.beginTransaction();

		UserEntity usermodel = new UserEntity();
		usermodel.setEMail("foo@mail.com");
		usermodel.setSalt("tweedledee");
		usermodel.createPassword("blah");
		addEntity(usermodel);

		assertTrue(usermodel.matchPassword("blah"));
		assertFalse(usermodel.matchPassword("tweedle"));

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
