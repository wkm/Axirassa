
package test.packetkit;

import static org.junit.Assert.assertEquals;
import static packetkit.PacketUtilities.assertArrayEquals;
import static packetkit.PacketUtilities.assertPacketEquals;

import org.junit.Test;

import packetkit.AbstractPacketTest;
import packetkit.PacketUtilities;
import packetkit.exceptions.PacketKitException;
import packetkit.headers.ICMPHeader;
import packetkit.headers.ICMPType;
import packetkit.headers.IPHeader;
import packetkit.headers.PacketSkeletonRegistry;


public class TestICMPHeader extends AbstractPacketTest {
	@Test
	public void construct() throws PacketKitException {
		ICMPHeader icmp = new ICMPHeader();

		icmp.setType(ICMPType.ECHO_REQUEST);
		icmp.setCode(0);
		icmp.setIdentifier(1);
		icmp.setSequenceNumber(14);
		icmp.setData(getBytes("IcmpEchoData"));

		assertPacketEquals(getProperty("IcmpEchoRequest"), icmp.construct());

		System.out.println(PacketSkeletonRegistry.getInstance().retrieve(IPHeader.class).toString());
	}


	@Test
	public void deconstruct() throws PacketKitException {
		ICMPHeader icmp = new ICMPHeader();
		icmp.deconstruct(PacketUtilities.parseHexDump(getProperty("IcmpEchoRequest")));

		assertEquals(ICMPType.ECHO_REQUEST, icmp.getType());
		assertEquals(0, icmp.getCode());
		assertEquals(1, icmp.getIdentifier());
		assertEquals(14, icmp.getSequenceNumber());

		assertArrayEquals(PacketUtilities.parseHexDump(getProperty("IcmpEchoData")), icmp.getData());
	}
}
