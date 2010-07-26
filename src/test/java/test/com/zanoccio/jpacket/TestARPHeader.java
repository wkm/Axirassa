
package test.com.zanoccio.jpacket;

import org.junit.Test;

import com.zanoccio.jpacket.IP4Address;
import com.zanoccio.jpacket.MACAddress;
import com.zanoccio.jpacket.exceptions.JPacketException;
import com.zanoccio.jpacket.headers.ARPHardwareType;
import com.zanoccio.jpacket.headers.ARPHeader;
import com.zanoccio.jpacket.headers.ARPProtocolType;
import com.zanoccio.jpacket.mock.MockNetworkInterface;

public class TestARPHeader {

	@Test
	public void construct() throws JPacketException {
		ARPHeader arp = new ARPHeader();

		arp.setHardwareType(ARPHardwareType.ETHERNET);
		arp.setProtocolType(ARPProtocolType.IP4);
		arp.setTargetMAC(MACAddress.BROADCAST);
		arp.setTargetIP(IP4Address.EMPTY);

		arp.associate(new MockNetworkInterface());
		arp.construct();
	}
}
