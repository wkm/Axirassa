
package test.packetkit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import packetkit.MACAddress;


public class TestMACAddress {

	@Test
	public void statics() {
		assertEquals("00:00:00:00:00:00", MACAddress.EMPTY.toString());
		assertEquals("ff:ff:ff:ff:ff:ff", MACAddress.BROADCAST.toString());
	}


	@Test
	public void construction() {
		MACAddress mac = MACAddress.parse("00:0a:0B:ff:FF:00");
		assertNotNull(mac);
		assertEquals("00:0a:0b:ff:ff:00", mac.toString());

		assertNull(MACAddress.parse("00"));
		assertNull(MACAddress.parse("00:00:00:00:00"));
		assertNull(MACAddress.parse("00:00:00:00:00:00:00"));
		assertNull(MACAddress.parse("00:00:00:00:00:00:"));
		assertNull(MACAddress.parse(":::::"));
		assertNull(MACAddress.parse("00:0a:0B:FF:FFF:00"));

		// check for overflow
		assertEquals("ff:ff:ff:ff:ff:ff", MACAddress.parse("ff:ff:ff:ff:ff:ff").toString());
	}


	@SuppressWarnings("boxing")
	@Test
	public void comparison() {
		MACAddress mac1 = MACAddress.parse("00:0a:0B:ff:FF:00");
		MACAddress mac2 = MACAddress.parse("00:0a:0b:ff:ff:00");
		MACAddress mac3 = MACAddress.parse("00:bb:cc:dd:ee:ff");

		assertNotNull(mac1);
		assertNotNull(mac2);
		assertNotNull(mac3);

		assertEquals(mac1, mac2);
		assertFalse(mac1.equals(mac3));
		assertFalse(mac2.equals(mac3));

		assertEquals(mac1.hashCode(), mac2.hashCode());
		assertFalse(mac1.hashCode() == mac3.hashCode());
		assertFalse(mac2.hashCode() == mac3.hashCode());
	}
}
