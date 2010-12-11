
package test.packetkit;

import static org.junit.Assert.assertEquals;
import static packetkit.PacketUtilities.assertPacketEquals;

import org.junit.Test;

import packetkit.IP4Address;
import packetkit.MACAddress;
import packetkit.NetworkInterface;
import packetkit.PacketUtilities;
import packetkit.exceptions.PacketKitException;
import packetkit.headers.ARPHardwareType;
import packetkit.headers.ARPHeader;
import packetkit.headers.ARPProtocolType;
import packetkit.mock.MockNetworkInterface;


public class TestARPHeader {

	static public String HEXDUMP_1 = "0000  00 01 08 00 06 04 00 01  de ad de ad be ef 12 34\n"
	        + "0010  56 78 ff ff ff ff ff ff  00 00 00 00";


	@Test
	public void construct() throws PacketKitException {
		ARPHeader arp = new ARPHeader();

		arp.setHardwareType(ARPHardwareType.ETHERNET);
		arp.setProtocolType(ARPProtocolType.IP4);
		arp.setTargetMAC(MACAddress.BROADCAST);
		arp.setTargetIP(IP4Address.EMPTY);

		arp.associate(new MockNetworkInterface());
		assertPacketEquals(HEXDUMP_1, arp);
	}


	@Test
	public void deconstruct() throws PacketKitException {
		ARPHeader arp = new ARPHeader();
		arp.deconstruct(PacketUtilities.parseHexDump(HEXDUMP_1));

		// test normal fields
		assertEquals(ARPHardwareType.ETHERNET, arp.getHardwareType());
		assertEquals(ARPProtocolType.IP4, arp.getProtocolType());
		assertEquals(MACAddress.BROADCAST, arp.getTargetMAC());
		assertEquals(IP4Address.EMPTY, arp.getTargetIP());

		// test autowired fields
		NetworkInterface mock = new MockNetworkInterface();
		assertEquals(mock.getIP4Address(), arp.getSenderIP());
		assertEquals(mock.getMACAddress(), arp.getSenderMAC());

	}
}
