
package test.com.zanoccio.packetkit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.zanoccio.packetkit.IP4Address;

public class TestIP4Address {

	@Test
	public void statics() {
		assertEquals("255.255.255.255", IP4Address.BROADCAST.toString());
		assertEquals("127.0.0.1", IP4Address.LOCALHOST.toString());
		assertEquals("0.0.0.0", IP4Address.EMPTY.toString());
	}


	@Test
	public void construction() {
		IP4Address ip4 = IP4Address.parse("127.0.0.1");
		assertNotNull(ip4);
		assertEquals("127.0.0.1", ip4.toString());

		assertNull(IP4Address.parse("127.0.0"));
		assertNull(IP4Address.parse("127.0.0.0.1"));
		assertNull(IP4Address.parse("127.-1.0.1"));
		assertNull(IP4Address.parse("127..."));
	}


	@Test
	public void comparison() {
		IP4Address ip1 = IP4Address.parse("192.168.1.1");
		IP4Address ip2 = IP4Address.parse("192.168.001.001");
		IP4Address ip3 = IP4Address.parse("192.168.0.1");

		assertNotNull(ip1);
		assertNotNull(ip2);
		assertNotNull(ip3);

		assertEquals(ip1, ip2);
		assertFalse(ip1.equals(ip3));
		assertFalse(ip2.equals(ip3));

		assertEquals(ip1.hashCode(), ip2.hashCode());
		assertFalse(ip1.hashCode() == ip3.hashCode());
		assertFalse(ip2.hashCode() == ip3.hashCode());
	}
}
