
package test.com.zanoccio.axirassa.pinger;

import java.io.IOException;

import org.jnetpcap.packet.RegistryHeaderErrors;
import org.junit.Test;

import com.zanoccio.axirassa.services.exceptions.PingServiceException;
import com.zanoccio.axirassa.services.pingservice.PingService;

public class PingServiceTest {
	@Test
	public void execute() throws PingServiceException, RegistryHeaderErrors, IOException {
		PingService pinger = new PingService();
		pinger.setHost("localhost");

		pinger.execute();
	}
}
