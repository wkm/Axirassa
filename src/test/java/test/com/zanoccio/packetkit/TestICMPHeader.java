
package test.com.zanoccio.packetkit;

import static com.zanoccio.packetkit.PacketUtilities.assertPacketEquals;

import org.junit.Test;

import com.zanoccio.packetkit.AbstractPacketTest;
import com.zanoccio.packetkit.exceptions.PacketKitException;
import com.zanoccio.packetkit.headers.ICMPHeader;
import com.zanoccio.packetkit.headers.ICMPType;

public class TestICMPHeader extends AbstractPacketTest {
	@Test
	public void test() throws PacketKitException {
		ICMPHeader icmp = new ICMPHeader();

		icmp.setType(ICMPType.ECHO_REQUEST);
		icmp.setCode(0);
		icmp.setIdentifier(1);
		icmp.setSequenceNumber(13);
		icmp.setData(getBytes("IcmpEchoData"));

		assertPacketEquals(getProperty("IcmpEchoRequest"), icmp.construct());
	}
}
