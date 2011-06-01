
package test.axirassa.flows;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.junit.Test;

import axirassa.model.UserEmailAddressEntity;
import axirassa.model.UserEntity;
import axirassa.model.flows.AddEmailAddressFlow;
import axirassa.services.email.EmailTemplate;
import axirassa.util.test.AbstractUnitTest;
import axirassa.webapp.services.EmailNotifyService;

public class TestAddEmailAddressFlow extends AbstractUnitTest {
	@Inject
	private AddEmailAddressFlow addEmailAddressFlow;

	@Inject
	private EmailNotifyService emailer;

	@Inject
	private Session session;


	@Test
	public void addEmail() throws Exception {
		UserEntity user = new UserEntity();

		addEmailAddressFlow.setUserEntity(user);
		addEmailAddressFlow.setEmailAddress("who1@foo2.com");
		addEmailAddressFlow.execute();

		verify(emailer).startMessage(EmailTemplate.USER_ADD_EMAIL);
		verify(emailer).setToAddress("who1@foo2.com");
		verify(emailer).addAttribute("axlink", "https://axirassa/");
		verify(emailer).send();
		verifyNoMoreInteractions(emailer);

		verify(session).save(isA(UserEmailAddressEntity.class));
		verifyNoMoreInteractions(session);
	}
}
