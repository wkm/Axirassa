
package test.axirassa.webapp.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Test;

import axirassa.dao.UserDAO;
import axirassa.dao.UserEmailAddressDAO;
import axirassa.model.UserEmailAddressEntity;
import axirassa.model.flows.CreateUserFlow;
import axirassa.util.test.AbstractIntegrationTest;
import axirassa.webapp.services.UserAuthenticationInfo;
import axirassa.webapp.services.UserCredentialsMatcher;

public class TestUserCredentialsMatcher extends AbstractIntegrationTest {

	@Inject
	private Session database;

	@Inject
	private UserDAO userDAO;

	@Inject
	private UserEmailAddressDAO addressDAO;

	@Inject
	private CreateUserFlow createUser;


	@Test
	public void createUsers() throws Exception {
		createUser.setEmail("who1@foo.com");
		createUser.setPassword("password");
		createUser.execute();

		createUser.setEmail("who2@foo.com");
		createUser.setPassword("password");
		createUser.execute();

		Query q = database.createQuery("from UserEntity");
		assertEquals(2, q.list().size());

		UserCredentialsMatcher matcher = new UserCredentialsMatcher(database);
		UserEmailAddressEntity email = addressDAO.getPrimaryEmail(userDAO.getUserByEmail("who1@foo.com"));
		UserAuthenticationInfo authinfo = UserAuthenticationInfo.createInfoFromEntity(email);
		UsernamePasswordToken token1 = new UsernamePasswordToken("who1@foo.com", "password");
		assertTrue(matcher.doCredentialsMatch(token1, authinfo));
	}

}
