
package test.axirassa.webapp.pages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import java.util.LinkedHashMap;

import org.apache.tapestry5.dom.Document;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.junit.Test;

import axirassa.dao.UserEmailAddressDAO;
import axirassa.model.UserEntity;
import axirassa.util.test.TapestryPageTest;
import axirassa.webapp.services.EmailNotifyService;

import com.formos.tapestry.xpath.TapestryXPath;

public class TestUserSettingsPage extends TapestryPageTest {
	@Inject
	UserEmailAddressDAO emailDAO;

	@Inject
	EmailNotifyService emailNotifier;


	@Test
	public void changePassword() throws Exception {
		createUser("who@foo.com", "123");
		loginUser("who@foo.com", "123");

		Document result = clickSubmitByValue("user/settings", "Change password", new LinkedHashMap<String, String>() {
			{
				put("txtfield", "123");
				put("txtfield_0", "1234");
				put("txtfield_1", "1234");
			}
		});

		Element notification = TapestryXPath.xpath("//*[@class='notification']").selectSingleElement(result);
		assertEquals("Your password was successfully changed.", getElementText(notification));

		// ensure the password was changed in the database
		UserEntity userEntity = emailDAO.getUserByEmail("who@foo.com");
		assertFalse(userEntity.matchPassword("123"));
		assertTrue(userEntity.matchPassword("1234"));

		// ensure a message was posted (details are verified in
		// ChangePasswordFlow)
		verify(emailNotifier).send();
	}
}
