
package test.packetkit;

import static packetkit.PacketUtilities.assertPacketEquals;

import org.junit.Test;

import packetkit.IP4Address;
import packetkit.exceptions.PacketKitException;
import packetkit.frames.Frame;
import packetkit.headers.ARPHeader;
import packetkit.headers.EtherType;
import packetkit.headers.EthernetHeader;
import packetkit.mock.MockNetworkInterface;


public class TestFrame {

	@Test
	public void whoHasFrame() throws PacketKitException {
		Frame whohas = new Frame();

		EthernetHeader macheader = EthernetHeader.broadcast();
		macheader.setType(EtherType.ARP);
		whohas.addHeader(macheader);

		ARPHeader arpheader = ARPHeader.whoHas(IP4Address.parse("192.168.1.8"));
		whohas.addHeader(arpheader);

		whohas.associate(new MockNetworkInterface());

		assertPacketEquals("0000  ff ff ff ff ff ff de ad  de ad be ef 08 06 00 01\n"
		                           + "0010  08 00 06 04 00 01 de ad  de ad be ef 12 34 56 78\n"
		                           + "0020  00 00 00 00 00 00 c0 a8  01 08",
		                   whohas);
	}
}
