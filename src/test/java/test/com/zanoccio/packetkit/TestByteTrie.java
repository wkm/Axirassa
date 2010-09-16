
package test.com.zanoccio.packetkit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.zanoccio.packetkit.ByteTrie;

public class TestByteTrie {
	@Test
	public void testTrie() {
		ByteTrie<String> trie = new ByteTrie<String>();
		trie.add(new byte[] { 0, 8, 9 }, "cat");
		trie.add(new byte[] { 2, 3 }, "dog");
		trie.add(new byte[] { 1 }, "furball");
		trie.add(new byte[] { 0, 8, 9, 0 }, "woofer");
		trie.add(new byte[] { 1, 1, 1, 1, 1, 1, 1, 1 }, "horsey");

		assertNull(trie.get(new byte[] { 0 }));
		assertNull(trie.get(new byte[] { 2 }));
		assertNull(trie.get(new byte[] { 0, 8 }));
		assertNull(trie.get(new byte[] { 0, 8, 9, 1 }));
		assertNull(trie.get(new byte[] { 5 }));
		assertNull(trie.get(new byte[] { 0, 9 }));
		assertNull(trie.get(new byte[] { 2, 3, 5 }));

		assertEquals("cat", trie.get(new byte[] { 0, 8, 9 }));
		assertEquals("dog", trie.get(new byte[] { 2, 3 }));
		assertEquals("furball", trie.get(new byte[] { 1 }));
		assertEquals("woofer", trie.get(new byte[] { 0, 8, 9, 0 }));
		assertEquals("horsey", trie.get(new byte[] { 1, 1, 1, 1, 1, 1, 1, 1 }));

		assertEquals("cat", trie.get(new byte[] { 1, 0, 8, 9, 2 }, 1, 3));

		//
		// benchmark
		//

		// byte[] key = new byte[] { 1, 1, 1, 1, 1, 1, 1, 1 };
		// long start = System.currentTimeMillis();
		//
		// for (int i = 0; i < 35000000; i++)
		// trie.get(key);
		//
		// System.out.println("elapsed: " + (System.currentTimeMillis() -
		// start));
	}
}
