
package test.axirassa.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.junit.Test;

import axirassa.model.UserEntity;
import axirassa.model.exception.NoSaltException;
import axirassa.util.test.AbstractIntegrationTest;

public class TestUserEntity extends AbstractIntegrationTest {

	@Inject
	private Session database;


	@Test
	@CommitAfter
	public void userPassword() throws NoSaltException {
		UserEntity user = new UserEntity();
		user.createPassword("blah");

		assertTrue(user.matchPassword("blah"));
		assertFalse(user.matchPassword("blah "));
		assertFalse(user.matchPassword("blah123"));
		assertFalse(user.matchPassword("tweedle"));
	}


	@Test
	@CommitAfter
	public void userAutomaticSalting() {
		UserEntity user = new UserEntity();
		long start = System.currentTimeMillis();
		user.createPassword("password");
		System.out.println("PASSWORD IN: " + (System.currentTimeMillis() - start));
		database.save(user);
	}

}
