
package test.com.zanoccio.packetkit;

import static com.zanoccio.packetkit.PacketUtilities.assertPacketEquals;

import org.junit.Test;

import com.zanoccio.packetkit.PacketUtilities;
import com.zanoccio.packetkit.headers.ChecksumMethod;

public class TestChecksumMethod {
	@Test
	public void onescomplement() {
		assertPacketEquals("0000  21 0e", ChecksumMethod.ONESCOMPLEMENT.compute(PacketUtilities
		        .parseHexDump("01 00 F2 03 F4 F5 F6 F7 00 00")));
	}
}
