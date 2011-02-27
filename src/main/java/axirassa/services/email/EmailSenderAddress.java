
package axirassa.services.email;

public enum EmailSenderAddress {
	ACCOUNT("account@axirassa.com"),
	ALERT("alert@axirassa.com");

	private final String address;


	EmailSenderAddress(String address) {
		this.address = address;
	}


	public String getAddress() {
		return address;
	}
}
