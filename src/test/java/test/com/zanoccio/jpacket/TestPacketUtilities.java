
package test.com.zanoccio.jpacket;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.zanoccio.jpacket.PacketUtilities;

public class TestPacketUtilities {
	@Test
	public void toHexDump() {
		assertEquals("0000  ff ff ff ff ff ff ff 00  24 d7 11 bf 74 08 06 00\n" + "0010  01 08 06",
		             PacketUtilities.toHexDump(new byte[] {
		                     (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
		                     0x00, 0x24, (byte) 0xd7, 0x11, (byte) 0xbf, 0x74, 0x08, 0x06, 0x00, 0x01, 0x08, 0x06 }));
	}
}
