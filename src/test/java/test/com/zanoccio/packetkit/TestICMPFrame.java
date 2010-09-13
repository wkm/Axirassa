
package test.com.zanoccio.packetkit;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.zanoccio.packetkit.AbstractPacketTest;
import com.zanoccio.packetkit.IP4Address;
import com.zanoccio.packetkit.MACAddress;
import com.zanoccio.packetkit.PacketUtilities;
import com.zanoccio.packetkit.exceptions.PacketKitException;
import com.zanoccio.packetkit.frames.Frame;
import com.zanoccio.packetkit.headers.EtherType;
import com.zanoccio.packetkit.headers.EthernetHeader;
import com.zanoccio.packetkit.headers.ICMPHeader;
import com.zanoccio.packetkit.headers.ICMPType;
import com.zanoccio.packetkit.headers.IPHeader;
import com.zanoccio.packetkit.headers.IPProtocol;
import com.zanoccio.packetkit.headers.PacketHeader;
import com.zanoccio.packetkit.mock.MockNetworkInterface;

public class TestICMPFrame extends AbstractPacketTest {
	@Test
	public void construct() throws PacketKitException {
		Frame frame = new Frame();

		// ethernet header
		EthernetHeader ethernet = new EthernetHeader();
		ethernet.setDestination(MACAddress.parse("00:24:d7:11:bf:74"));
		ethernet.setSource(MACAddress.parse("00:1f:33:46:94:e8"));
		ethernet.setType(EtherType.IP4);
		frame.addHeader(ethernet);

		// IP header
		IPHeader ip = new IPHeader();
		ip.setVersion(4);
		ip.setHeaderLength(20);
		ip.setServices(0);
		ip.setTotalLength(60);
		ip.setIdentification(15097);
		ip.setFlags(0);
		ip.setFragmentOffset(0);
		ip.setTimeToLive(51);
		ip.setProtocol(IPProtocol.ICMP);
		ip.setSource(IP4Address.parse("74.125.95.99"));
		ip.setDestination(IP4Address.parse("192.168.1.3"));
		frame.addHeader(ip);

		ICMPHeader icmp = new ICMPHeader();
		icmp.setType(ICMPType.ECHO_REPLY);
		icmp.setCode(0);
		icmp.setIdentifier(1);
		icmp.setSequenceNumber(17);
		icmp.setData(PacketUtilities.parseHexDump(getProperty("IcmpData")));
		frame.addHeader(icmp);

		frame.associate(new MockNetworkInterface());

		PacketUtilities.assertPacketEquals(getProperty("IcmpFrame"), frame.construct());
	}


	@Test
	public void deconstruct() throws PacketKitException {
		Frame frame = new Frame();
		List<PacketHeader> headers = new ArrayList<PacketHeader>(3);
		headers.add(new EthernetHeader());
		headers.add(new IPHeader());
		headers.add(new ICMPHeader());

		frame.deconstruct(headers, PacketUtilities.parseHexDump(getProperty("IcmpFrame")));

		headers = frame.getHeaders();
		for (PacketHeader header : headers) {
			System.out.println("header: " + header);
		}
	}
}
