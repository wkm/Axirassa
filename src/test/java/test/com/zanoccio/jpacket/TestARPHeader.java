
package test.com.zanoccio.jpacket;

import org.junit.Test;

import com.zanoccio.jpacket.headers.ARPHardwareType;
import com.zanoccio.jpacket.headers.ARPHeader;
import com.zanoccio.jpacket.headers.ARPProtocolType;

public class TestARPHeader {

	@Test
	public void construct() {
		ARPHeader arp = new ARPHeader();

		arp.setHardwareType(ARPHardwareType.ETHERNET);
		arp.setProtocolType(ARPProtocolType.IP4);
	}
}
