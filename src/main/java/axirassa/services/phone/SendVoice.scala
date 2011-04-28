
package axirassa.services.phone;

public class SendVoice extends TropoSender {
	public SendVoice(String number, String extension, String message) {
		setPhoneNumber(number);
		setMessage(message);
	}


	@Override
	public String getToken() {
		return "0d10e8fb91e4c747bcd5f41d2e8e164f81ffec5643a61f2c6119580304223b7bb30318d19a22816f92a649d8";
	}
}
