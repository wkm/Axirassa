
package test.axirassa.pinger;

import java.util.ArrayList;
import java.util.List;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.junit.BeforeClass;
import org.junit.Test;

import axirassa.util.PcapLoader;

// -Djava.library.path="${workspace_loc:X}\src\main\resources\"
public class NICMacAddressTest {
	@BeforeClass
	public static void loadLibraries() throws Exception {
		PcapLoader.load();
	}


	@Test
	public void run() {
		List<PcapIf> devices = new ArrayList<PcapIf>();
		StringBuilder error = new StringBuilder();

		int r = Pcap.findAllDevs(devices, error);
		if (r == Pcap.NOT_OK || devices.isEmpty()) {
			System.err.printf("Can't find devices: %s", error);
			return;
		}

		System.out.println(devices);
	}
}
