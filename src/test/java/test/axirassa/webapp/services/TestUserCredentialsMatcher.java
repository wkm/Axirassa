
package test.axirassa.webapp.services;

import static org.junit.Assert.assertTrue;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.BeforeClass;
import org.junit.Test;

import axirassa.model.UserEntity;
import axirassa.model.exception.NoSaltException;
import axirassa.util.AbstractDomainTest;
import axirassa.webapp.services.UserAuthenticationInfo;
import axirassa.webapp.services.UserCredentialsMatcher;

public class TestUserCredentialsMatcher {
	public static Session session = AbstractDomainTest.hibernateSession();


	@BeforeClass
	public static void createUsers() throws NoSaltException {
		session.beginTransaction();
		UserEntity u1 = new UserEntity();
		u1.setEmail("charles@gmail.com");
		u1.createPassword("password");
		session.save(u1);

		UserEntity u2 = new UserEntity();
		u2.setEmail("edgar@gmail.com");
		u2.createPassword("Edgar's Awesomeness!");
		session.save(u2);
		session.getTransaction().commit();

		Query q = session.createQuery("from UserEntity");
		System.out.println("Users: " + q.list());
	}


	@Test
	public void testMatcher() {
		UserCredentialsMatcher matcher = new UserCredentialsMatcher(session);
		UserAuthenticationInfo authinfo;

		authinfo = UserAuthenticationInfo.createInfoFromEntity(UserEntity.getUserByEmail(session, "charles@gmail.com"));

		UsernamePasswordToken token1 = new UsernamePasswordToken("charles@gmail.com", "password");
		assertTrue(matcher.doCredentialsMatch(token1, authinfo));
	}
}
