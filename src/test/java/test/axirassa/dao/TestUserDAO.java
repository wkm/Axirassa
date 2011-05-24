
package test.axirassa.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.junit.Test;

import axirassa.dao.UserDAO;
import axirassa.model.UserEmailAddressEntity;
import axirassa.model.UserEntity;
import axirassa.util.test.AbstractIntegrationTest;

public class TestUserDAO extends AbstractIntegrationTest {
	@Inject
	private Session database;

	@Inject
	private UserDAO userDAO;


	@Test
	public void isEmailRegistered() {
		database.beginTransaction();
		UserEmailAddressEntity email;
		UserEntity user = new UserEntity();
		user.createPassword("123");
		database.save(user);

		email = new UserEmailAddressEntity();
		email.setUser(user);
		email.setEmail("who@foo.com");
		database.save(email);

		email = new UserEmailAddressEntity();
		email.setUser(user);
		email.setEmail("WHO+1@FOO.com");
		database.save(email);
		database.getTransaction().commit();

		assertTrue(userDAO.isEmailRegistered("who@foo.com"));
		assertTrue(userDAO.isEmailRegistered("who+1@foo.com"));
		assertFalse(userDAO.isEmailRegistered("who+2@foo.com"));

		assertTrue(userDAO.isEmailRegistered("WHO@FOO.COM"));
		assertTrue(userDAO.isEmailRegistered("WHO+1@FOO.COM"));
	}


	@Test
	public void getUserByEmail() {
		assertNotNull(userDAO.getUserByEmail("who@foo.com"));
		assertNotNull(userDAO.getUserByEmail("who+1@foo.com"));
		assertNotNull(userDAO.getUserByEmail("WHO@FOO.COM"));
		assertNotNull(userDAO.getUserByEmail("WHO+1@FOO.COM"));

		assertNull(userDAO.getUserByEmail("who"));
		assertNull(userDAO.getUserByEmail("who@foocom"));
		assertNull(userDAO.getUserByEmail("who1@foo.com"));
		assertNull(userDAO.getUserByEmail("what@foo.com"));
	}
}
