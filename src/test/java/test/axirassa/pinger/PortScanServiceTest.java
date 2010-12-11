
package test.axirassa.pinger;

import java.io.IOException;

import org.junit.Test;

import axirassa.services.PortScanService;

public class PortScanServiceTest {

	@Test
	public void authentication() throws IOException, InterruptedException {
		PortScanService service = new PortScanService();
		service.setAddress("127.0.0.1");

		service.execute();

		System.out.println("Open ports: " + service.getOpenPorts());
	}
}
