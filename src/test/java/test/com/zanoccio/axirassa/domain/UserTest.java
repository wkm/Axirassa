
package test.com.zanoccio.axirassa.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Test;

import com.zanoccio.axirassa.domain.User;
import com.zanoccio.axirassa.domain.exception.NoSaltException;
import com.zanoccio.axirassa.util.HibernateUtil;
import com.zanoccio.axirassa.util.SchemaTest;

public class UserTest extends SchemaTest {
	@Test
	public void userPassword() throws NoSaltException {
		User user = new User();
		user.setEMail("foo@mail.com");
		user.setSalt("tweedledee");
		user.createPassword("blah");
		addEntity(user);

		assertTrue(user.matchPassword("blah"));
		assertFalse(user.matchPassword("tweedle"));

		Session session = HibernateUtil.getSession();
		session.beginTransaction();

		Query query = session.createQuery("from User");
		List results = query.list();
		User storeduser = (User) results.get(0);
		session.getTransaction().commit();

		assertTrue(storeduser.matchPassword("blah"));
		assertFalse(storeduser.matchPassword("tweedle"));
		assertFalse(storeduser.matchPassword("*!@#HJKNMoiu9"));
	}
}
