
package test.axirassa.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;

import axirassa.dao.UserEmailAddressDAO;
import axirassa.model.UserEmailAddressEntity;
import axirassa.model.UserEntity;
import axirassa.util.test.AbstractIntegrationTest;

public class TestUserEmailAddressDAO extends AbstractIntegrationTest {
	@Inject
	private Session database;

	@Inject
	private UserEmailAddressDAO emailDAO;


	@Before
	public void init() {
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
	}


	@Test
	public void isEmailRegistered() {

		assertTrue(emailDAO.isEmailRegistered("who@foo.com"));
		assertTrue(emailDAO.isEmailRegistered("who+1@foo.com"));
		assertFalse(emailDAO.isEmailRegistered("who+2@foo.com"));

		assertTrue(emailDAO.isEmailRegistered("WHO@FOO.COM"));
		assertTrue(emailDAO.isEmailRegistered("WHO+1@FOO.COM"));
	}


	@Test
	public void getUserByEmail() {
		assertNotNull(emailDAO.getUserByEmail("who@foo.com"));
		assertNotNull(emailDAO.getUserByEmail("who+1@foo.com"));
		assertNotNull(emailDAO.getUserByEmail("WHO@FOO.COM"));
		assertNotNull(emailDAO.getUserByEmail("WHO+1@FOO.COM"));

		assertNull(emailDAO.getUserByEmail("who"));
		assertNull(emailDAO.getUserByEmail("who@foocom"));
		assertNull(emailDAO.getUserByEmail("who1@foo.com"));
		assertNull(emailDAO.getUserByEmail("what@foo.com"));
	}
}
