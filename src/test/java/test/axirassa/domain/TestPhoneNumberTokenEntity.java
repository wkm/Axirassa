
package test.axirassa.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.junit.Test;
import org.junit.runner.RunWith;

import axirassa.ioc.IocIntegrationTestRunner;
import axirassa.model.UserEmailAddressEntity;
import axirassa.model.UserEntity;
import axirassa.model.UserPhoneNumberEntity;
import axirassa.model.flows.CreateUserFlow;

@RunWith(IocIntegrationTestRunner.class)
public class TestPhoneNumberTokenEntity {
	@Inject
	CreateUserFlow createUserFlow;

	@Inject
	Session database;


	@Test
	@CommitAfter
	public void getTokensByPhoneNumber() {
		UserEntity user = new UserEntity();
		user.createPassword("blah");
		database.save(user);

		UserEmailAddressEntity email = new UserEmailAddressEntity();
		email.setEmail("who@foo.com");
		email.setPrimaryEmail(true);
		email.setUser(user);

		database.save(email);

		UserPhoneNumberEntity phoneNumber = new UserPhoneNumberEntity();
		phoneNumber.setPhoneNumber("1234567890");
		phoneNumber.setUser(user);
		database.save(phoneNumber);

		assertNotNull(phoneNumber.getToken());

		phoneNumber.setToken("A1B2C3D4");
		assertEquals("A1-B2-C3-D4", phoneNumber.getFormattedToken());
	}
}
