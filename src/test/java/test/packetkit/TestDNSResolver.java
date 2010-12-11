
package test.packetkit;

import org.junit.Test;

import packetkit.DNSResolver;


public class TestDNSResolver {

	@Test
	public void zanoccio() {
		System.out.println(DNSResolver.lookup("zanoccio.com", "A"));
	}

}
