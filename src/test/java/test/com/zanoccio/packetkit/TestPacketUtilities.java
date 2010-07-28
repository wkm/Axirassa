
package test.com.zanoccio.packetkit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.zanoccio.packetkit.PacketUtilities;

public class TestPacketUtilities {

	private static String hexdump = "0000  ff ff ff ff ff ff ff 00  24 d7 11 bf 74 08 06 00\n" + "0010  01 08 06";
	private static byte[] hexarray = new byte[] {
	        (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x00, 0x24,
	        (byte) 0xd7, 0x11, (byte) 0xbf, 0x74, 0x08, 0x06, 0x00, 0x01, 0x08, 0x06 };


	@Test
	public void toHexDump() {
		assertEquals(hexdump, PacketUtilities.toHexDump(hexarray));
	}


	@Test
	public void parseHexDump() {
		PacketUtilities.parseHexDump(hexdump);
		assertEquals(hexdump,
		             PacketUtilities.toHexDump(PacketUtilities.parseHexDump(PacketUtilities.toHexDump(hexarray))));
		assertEquals(hexdump, PacketUtilities.toHexDump(PacketUtilities.parseHexDump(PacketUtilities
		        .toHexDumpFragment(hexarray))));
	}
}
