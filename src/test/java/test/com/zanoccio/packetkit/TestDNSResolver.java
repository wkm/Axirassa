
package test.com.zanoccio.packetkit;

import org.junit.Test;

import com.zanoccio.packetkit.DNSResolver;

public class TestDNSResolver {

	@Test
	public void zanoccio() {
		System.out.println(DNSResolver.lookup("zanoccio.com", "A"));
	}

}
