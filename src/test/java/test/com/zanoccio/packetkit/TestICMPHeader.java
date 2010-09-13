
package test.com.zanoccio.packetkit;

import static com.zanoccio.packetkit.PacketUtilities.assertArrayEquals;
import static com.zanoccio.packetkit.PacketUtilities.assertPacketEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.zanoccio.packetkit.AbstractPacketTest;
import com.zanoccio.packetkit.PacketUtilities;
import com.zanoccio.packetkit.exceptions.PacketKitException;
import com.zanoccio.packetkit.headers.ICMPHeader;
import com.zanoccio.packetkit.headers.ICMPType;
import com.zanoccio.packetkit.headers.IPHeader;
import com.zanoccio.packetkit.headers.PacketSkeletonRegistry;

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
