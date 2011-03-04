
package test.axirassa.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import axirassa.model.UserEntity;
import axirassa.model.UserPhoneNumberEntity;
import axirassa.util.AbstractDomainTest;

public class TestPhoneNumberTokenEntity extends AbstractDomainTest {
	@Test
	public void getTokensByPhoneNumber() {
		session.beginTransaction();

		UserEntity user = new UserEntity();
		user.setEmail("foo@who.com");
		user.createPassword("blah");
		session.save(user);

		UserPhoneNumberEntity phoneNumber = new UserPhoneNumberEntity();
		phoneNumber.setPhoneNumber("1234567890");
		phoneNumber.setUser(user);
		session.save(phoneNumber);

		assertNotNull(phoneNumber.getToken());

		phoneNumber.setToken("A1B2C3D4");
		assertEquals("A1-B2-C3-D4", phoneNumber.getFormattedToken());

		session.getTransaction().commit();
	}
}
