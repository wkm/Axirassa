
package test.axirassa.webapp.pages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;

import java.util.LinkedHashMap;

import org.apache.tapestry5.dom.Document;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.junit.Test;

import axirassa.dao.UserEmailAddressDAO;
import axirassa.model.UserEmailAddressEntity;
import axirassa.model.UserEntity;
import axirassa.util.test.TapestryPageTest;
import axirassa.webapp.services.EmailNotifyService;

public class TestAddEmailAddressPage extends TapestryPageTest {
	@Inject
	UserEmailAddressDAO emailDAO;

	@Inject
	EmailNotifyService emailNotifier;


	@Test
	public void addEmailAddress() throws Exception {
		createUser("who@foo.com", "123");
		loginUser("who@foo.com", "123");

		Document result = clickSubmitByValue("user/addemailaddress", "Add e-mail address",
		                                     new LinkedHashMap<String, String>() {
			                                     {
				                                     put("emailField", "what@foo.com");
				                                     put("emailConfirmField", "what@foo.com");
			                                     }
		                                     });

		System.out.println("RESULT: " + result);
		verify(emailNotifier).send();

		UserEntity user = emailDAO.getUserByEmail("what@foo.com");
		assertNotNull(user);
		assertEquals("who@foo.com", emailDAO.getPrimaryEmail(user).getEmail());

		UserEmailAddressEntity email = emailDAO.getByEmail("what@foo.com");
		assertFalse(email.isPrimaryEmail());
		assertFalse(email.isVerified());
	}
}
