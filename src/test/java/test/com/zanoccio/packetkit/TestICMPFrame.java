
package test.com.zanoccio.packetkit;

import org.junit.Test;

import com.zanoccio.packetkit.AbstractPacketTest;
import com.zanoccio.packetkit.MACAddress;
import com.zanoccio.packetkit.frames.Frame;
import com.zanoccio.packetkit.headers.EtherType;
import com.zanoccio.packetkit.headers.EthernetHeader;

public class TestICMPFrame extends AbstractPacketTest {
	@Test
	public void test() {
		Frame frame = new Frame();

		// ethernet header
		EthernetHeader ethernet = new EthernetHeader();
		ethernet.setDestination(MACAddress.parse("00:24:d7:11:bf:74"));
		ethernet.setSource(MACAddress.parse("00:1f:33:46:94:e8"));
		ethernet.setType(EtherType.IP4);
		frame.addHeader(ethernet);
	}
}
