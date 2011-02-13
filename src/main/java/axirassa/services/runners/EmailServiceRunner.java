
package axirassa.services.runners;

import axirassa.services.Service;
import axirassa.services.email.EmailService;

public class EmailServiceRunner {
	public static void main(String[] args) throws Exception {
		Service service = new EmailService(null);
		service.execute();
	}
}
