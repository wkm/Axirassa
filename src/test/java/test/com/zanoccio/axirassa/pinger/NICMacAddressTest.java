
package test.com.zanoccio.axirassa.pinger;

import java.util.ArrayList;
import java.util.List;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.junit.BeforeClass;
import org.junit.Test;

import com.zanoccio.axirassa.util.PCap;

public class NICMacAddressTest {
	@BeforeClass
	public static void loadLibraries() {
		PCap.loadLibrary();
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
