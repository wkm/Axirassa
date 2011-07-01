
package test.axirassa.flows;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import axirassa.model.UserEmailAddressEntity;
import axirassa.model.UserEntity;
import axirassa.model.flows.CreateUserFlow;
import axirassa.services.email.EmailTemplate;
import axirassa.util.test.AbstractUnitTest;
import axirassa.webapp.services.EmailNotifyService;

public class TestCreateUserFlow extends AbstractUnitTest {

	@Inject
	private CreateUserFlow createUserFlow;

	@Inject
	private EmailNotifyService emailer;

	@Inject
	private Session session;





	class UserOrPrimaryEmailMatcher extends ArgumentMatcher<Object> {
		@Override
		public boolean matches(Object argument) {
			if (argument instanceof UserEntity) {
				return true;
			} else if (argument instanceof UserEmailAddressEntity) {
				UserEmailAddressEntity email = (UserEmailAddressEntity) argument;
				assertEquals(email.getEmail(), "who@foo.com");
				assertTrue(email.isPrimaryEmail());
				assertFalse(email.isVerified());
			} else
				return false;
			return true;
		}
	}


	@Test
	public void createUser() throws Exception {
		createUserFlow.setEmail("who@foo.com");
		createUserFlow.setPassword("password");
		createUserFlow.execute();

		// did we send an e-mail?
		verify(emailer).startMessage(EmailTemplate.USER_VERIFY_ACCOUNT);
		verify(emailer).setToAddress("who@foo.com");
		verify(emailer).addAttribute("axlink", "https://axirassa/");
		verify(emailer).send();
		verifyNoMoreInteractions(emailer);

		// did we save the user
		verify(session, times(2)).save(argThat(new UserOrPrimaryEmailMatcher()));
		verifyNoMoreInteractions(session);
	}
}
