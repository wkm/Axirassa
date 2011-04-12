
package axirassa.services.email;

import lombok.Getter;

public enum EmailSenderAddress {
	ACCOUNT("account@axirassa.com"),
	ALERT("alert@axirassa.com"),
	INTERNAL("internal@axirassa.com");

	@Getter
	private final String address;


	EmailSenderAddress (String address) {
		this.address = address;
	}
}
