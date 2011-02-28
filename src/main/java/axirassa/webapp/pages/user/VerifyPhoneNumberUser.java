
package axirassa.webapp.pages.user;

public class VerifyPhoneNumberUser {
	public Object onActivate() {
		return SettingsUser.class;
	}


	public Object onActivate(String token) {
		return this;
	}

}
