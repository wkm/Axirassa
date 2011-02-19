
package axirassa.services.email;

public enum EmailTemplate {
	USER_VERIFY_ACCOUNT("account/VerifyAccountEmail");

	public static final String BASE_LOCATION = "/axirassa/webapp/emails/";

	private String location;


	EmailTemplate(String location) {
		this.location = location;
	}


	public String getLocation() {
		return location;
	}


	public String getFullLocation() {
		return BASE_LOCATION + location + ".html";
	}
}
