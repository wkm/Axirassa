
package test.axirassa.dao;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.junit.Test;

import axirassa.dao.UserDAO;

public class TestUserEmailAddressDAO {
	@Inject
	private Session database;

	@Inject
	private UserDAO userDAO;


	@Test
	public void isEmailRegistered() {
		// empty for now
	}
}
