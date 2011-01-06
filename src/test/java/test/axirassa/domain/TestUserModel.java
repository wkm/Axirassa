
package test.axirassa.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.hibernate.Query;
import org.junit.Test;

import axirassa.domain.AccountUserModel;
import axirassa.domain.UserModel;
import axirassa.domain.exception.NoSaltException;
import axirassa.domainpaths.QuickRegisterPath;
import axirassa.util.AbstractDomainTest;

public class TestUserModel extends AbstractDomainTest {
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

		session.getTransaction().commit();
	}


	@Test
	public void userAutomaticSalting() throws NoSaltException {
		session.beginTransaction();
		UserModel user = new UserModel();
		user.setEMail("foo@bar.com");
		user.createPassword("password");
		addEntity(user);
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
