
package test.axirassa.flows;

import static org.mockito.Mockito.verify;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.junit.Test;

import axirassa.model.flows.CreateUserFlow;
import axirassa.services.email.EmailTemplate;
import axirassa.util.test.AbstractUnitTest;
import axirassa.webapp.services.EmailNotifyService;

public class TestCreateUserFlow extends AbstractUnitTest {

	@Inject
	private CreateUserFlow createUserFlow;

	@Inject
	private EmailNotifyService emailer;


	@Test
	public void createUser() {
		createUserFlow.setEmail("who@foo.com");
		createUserFlow.setPassword("password");
		createUserFlow.execute();

		verify(emailer).startMessage(EmailTemplate.USER_VERIFY_ACCOUNT);
		verify(emailer).setToAddress("who@foo.com");
		verify(emailer).addAttribute("axlink", "https://axirassa/");
	}

}
