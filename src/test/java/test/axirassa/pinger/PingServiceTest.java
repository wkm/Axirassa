
package test.axirassa.pinger;

import java.io.IOException;

import org.jnetpcap.packet.RegistryHeaderErrors;
import org.junit.Test;

import axirassa.services.exceptions.PingServiceException;
import axirassa.services.pingservice.PingService;

public class PingServiceTest {
	@Test
	public void execute() throws PingServiceException, RegistryHeaderErrors, IOException {
		PingService pinger = new PingService();
		pinger.setHost("localhost");

		pinger.execute();
	}
}
