
package test.axirassa.webapp.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import axirassa.dao.UserDAO;
import axirassa.dao.UserEmailAddressDAO;
import axirassa.ioc.IocIntegrationTestRunner;
import axirassa.model.UserEmailAddressEntity;
import axirassa.model.UserEntity;
import axirassa.webapp.services.UserAuthenticationInfo;
import axirassa.webapp.services.UserCredentialsMatcher;

@RunWith(IocIntegrationTestRunner.class)
public class TestUserCredentialsMatcher {

	@Inject
	private Session database;

	@Inject
	private UserDAO userDAO;

	@Inject
	private UserEmailAddressDAO addressDAO;


	@Before
	@CommitAfter
	public void createUsers() {
		database.beginTransaction();
		UserEntity user = new UserEntity();
		UserEmailAddressEntity email = new UserEmailAddressEntity();

		user.createPassword("password");
		database.save(user);

		email.setEmail("who1@foo.com");
		email.setPrimaryEmail(true);
		email.setUser(user);
		database.save(email);

		// create another user
		user = new UserEntity();
		user.createPassword("password");
		database.save(user);

		email = new UserEmailAddressEntity();
		email.setEmail("who2@foo.com");
		email.setPrimaryEmail(true);
		email.setUser(user);
		database.save(email);

		database.getTransaction().commit();

		Query q = database.createQuery("from UserEntity");
		System.out.println("Users: " + q.list());
		assertEquals(2, q.list().size());
	}


	@Test
	public void testMatcher() {
		UserCredentialsMatcher matcher = new UserCredentialsMatcher(database);
		UserAuthenticationInfo authinfo;
		UserEmailAddressEntity email = addressDAO.getPrimaryEmail(userDAO.getUserByEmail("who1@foo.com"));

		authinfo = UserAuthenticationInfo.createInfoFromEntity(email);

		UsernamePasswordToken token1 = new UsernamePasswordToken("who1@foo.com", "password");
		assertTrue(matcher.doCredentialsMatch(token1, authinfo));
	}
}
