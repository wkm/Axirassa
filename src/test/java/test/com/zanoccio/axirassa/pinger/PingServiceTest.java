
package test.com.zanoccio.axirassa.pinger;

import java.io.IOException;

import org.jnetpcap.packet.RegistryHeaderErrors;
import org.junit.Test;

import com.zanoccio.axirassa.services.PingService;
import com.zanoccio.axirassa.services.exceptions.PingServiceException;

public class PingServiceTest {
	@Test
	public void execute() throws PingServiceException, RegistryHeaderErrors, IOException {
		PingService pinger = new PingService();
		pinger.setHost("localhost");

		pinger.execute();
	}
}