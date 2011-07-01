
package test.axirassa.flows;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.junit.Test;

import axirassa.dao.UserEmailAddressDAO;
import axirassa.model.UserEmailAddressEntity;
import axirassa.model.UserEntity;
import axirassa.model.flows.CreateUserFlow;
import axirassa.model.flows.ResetPasswordFlow;
import axirassa.services.email.EmailTemplate;
import axirassa.util.test.AbstractUnitTest;
import axirassa.webapp.services.EmailNotifyService;

public class TestResetPasswordFlow extends AbstractUnitTest {

	@Inject
	private ResetPasswordFlow resetPassword;

	@Inject
	private EmailNotifyService emailer;

	@Inject
	private Session session;

	@Inject
	private CreateUserFlow createUser;

	@Inject
	private UserEmailAddressDAO emailDAO;


	@Test
	public void resetPassword() throws Exception {
		UserEntity user = new UserEntity();

		UserEmailAddressEntity email = new UserEmailAddressEntity();
		email.setEmail("who@foo.com");

		when(emailDAO.getPrimaryEmail(any(UserEntity.class))).thenReturn(email);

		resetPassword.setUserEntity(user);
		resetPassword.execute();

		verify(emailer).startMessage(EmailTemplate.USER_RESET_PASSWORD);
		verify(emailer).setToAddress("who@foo.com");
		verify(emailer).addAttribute("axlink", "https://axirassa/");
		verify(emailer).send();
		verifyNoMoreInteractions(emailer);
	}
}
