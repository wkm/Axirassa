
package test.com.zanoccio.jpacket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.zanoccio.jpacket.IP4Address;

public class TestIP4Address {

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
}
