
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
	public void structure() throws Exception {
		createUserAndLogin("who@foo.com", "123");

		Document page = renderPage("user/settings");

		ensureNoErrors(page);
		ensureHasElementById(page, "currentPasswordField");
		ensureHasElementById(page, "passwordField");
		ensureHasElementById(page, "confirmPasswordField");

		assertEquals("Current password", getLabelTextFor(page, "currentPasswordField"));
		assertEquals("New password", getLabelTextFor(page, "passwordField"));
		assertEquals("Confirm password", getLabelTextFor(page, "confirmPasswordField"));
	}


	@Test
	public void changePassword() throws Exception {
		createUserAndLogin("who@foo.com", "123");

		Document result = clickSubmitByValue("user/settings", "Change password", new LinkedHashMap<String, String>() {
			{
				put("currentPasswordField", "123");
				put("passwordField", "1234");
				put("confirmPasswordField", "1234");
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
