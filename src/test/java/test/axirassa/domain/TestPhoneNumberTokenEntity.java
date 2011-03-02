
package test.axirassa.domain;

import org.junit.Test;

import axirassa.model.PhoneNumberTokenEntity;
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

		PhoneNumberTokenEntity token = new PhoneNumberTokenEntity();
		token.setPhoneNumberEntity(phoneNumber);
		session.save(token);

		session.getTransaction().commit();

		System.out.println("phoneNumber: " + phoneNumber.getId());

		System.out.println("NUMBERS: " + PhoneNumberTokenEntity.getTokensByPhoneNumber(session, phoneNumber));
	}
}
