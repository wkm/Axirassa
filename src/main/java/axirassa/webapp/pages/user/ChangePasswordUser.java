
package axirassa.webapp.pages.user;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;

import axirassa.webapp.components.AxForm;
import axirassa.webapp.components.AxPasswordField;

@RequiresUser
public class ChangePasswordUser {

	@Property
	private final boolean hasToken = false;

	@Property
	private String currentPassword;

	@Property
	private String newPassword;

	@Property
	private String confirmPassword;

	@Component
	private AxPasswordField currentPasswordField;

	@Component
	private AxPasswordField confirmPasswordField;

	@Component
	private AxForm form;


	public void onValidateFromForm() {

	}


	@CommitAfter
	public Object onSuccess() {
		return SettingsUser.class;
	}

}
