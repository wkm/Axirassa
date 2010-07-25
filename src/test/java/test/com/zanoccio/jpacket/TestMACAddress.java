
package test.com.zanoccio.jpacket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.zanoccio.jpacket.MACAddress;

public class TestMACAddress {

	@Test
	public void construction() {
		MACAddress mac = MACAddress.parse("00:0a:0B:ff:FF:00");
		assertNotNull(mac);
		assertEquals(mac.toString(), "00:0a:0b:ff:ff:00");

		assertNull(MACAddress.parse("00"));
		assertNull(MACAddress.parse("00:00:00:00:00"));
		assertNull(MACAddress.parse("00:00:00:00:00:00:00"));
		assertNull(MACAddress.parse("00:00:00:00:00:00:"));
		assertNull(MACAddress.parse(":::::"));
		assertNull(MACAddress.parse("00:0a:0B:FF:FFF:00"));
	}
}
