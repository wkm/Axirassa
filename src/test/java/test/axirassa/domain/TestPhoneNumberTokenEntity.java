
package test.axirassa.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;

import axirassa.ioc.IocIntegrationTestRunner;
import axirassa.model.UserEntity;
import axirassa.model.UserPhoneNumberEntity;
import axirassa.model.flows.CreateUserFlow;
import axirassa.util.AbstractDomainTest;
import org.apache.tapestry5.ioc.annotations.Inject;

@RunWith(IocIntegrationTestRunner.class)
public class TestPhoneNumberTokenEntity extends AbstractDomainTest {
    @Inject
    CreateUserFlow createUserFlow;
    
	@Test
	public void getTokensByPhoneNumber () {
		session.beginTransaction();

        
        createUserFlow.setEmail("foo@who.com");
        createUserFlow.setPassword("blah");
        createUserFlow.execute();
        
		UserEntity user = createUserFlow.getUserEntity();
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
