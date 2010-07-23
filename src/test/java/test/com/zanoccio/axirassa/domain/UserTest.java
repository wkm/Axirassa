
package test.com.zanoccio.axirassa.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.hibernate.Query;
import org.junit.Test;

import com.zanoccio.axirassa.domain.AccountUserModel;
import com.zanoccio.axirassa.domain.UserModel;
import com.zanoccio.axirassa.domain.exception.NoSaltException;
import com.zanoccio.axirassa.domainpaths.QuickRegisterPath;
import com.zanoccio.axirassa.util.AbstractDomainTest;

public class UserTest extends AbstractDomainTest {
	@Test
	public void userPassword() throws NoSaltException {
		session.beginTransaction();

		UserModel usermodel = new UserModel();
		usermodel.setEMail("foo@mail.com");
		usermodel.setSalt("tweedledee");
		usermodel.createPassword("blah");
		addEntity(usermodel);

		assertTrue(usermodel.matchPassword("blah"));
		assertFalse(usermodel.matchPassword("tweedle"));

		// Query query = session.createQuery("from UserModel");
		// List results = query.list();
		// UserModel storeduser = (UserModel) results.get(0);
		//
		// assertTrue(storeduser.matchPassword("blah"));
		// assertFalse(storeduser.matchPassword("tweedle"));
		// assertFalse(storeduser.matchPassword("*!@#HJKNMoiu9"));
		// assertFalse(storeduser.matchPassword("\""));
		// assertFalse(storeduser.matchPassword("'"));

		session.getTransaction().commit();
	}


	@Test
	public void quickRegisterPath() throws NoSaltException {
		session.beginTransaction();
		new QuickRegisterPath("twit@mail.com", "boohoo").execute(session);

		Query query = session.createQuery("from AccountUserModel");
		List results = query.list();
		AccountUserModel accountuser = (AccountUserModel) results.get(0);

		session.getTransaction().commit();
	}
}
