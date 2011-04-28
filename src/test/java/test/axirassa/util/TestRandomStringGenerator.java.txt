
package test.axirassa.util;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import axirassa.util.RandomStringGenerator;

public class TestRandomStringGenerator {

	@Test
	public void testRandomStrings() {
		for (int i = 0; i < 2000; i++)
			assertTrue(RandomStringGenerator.getInstance().randomString(13).length() == 13);

		for (int i = 0; i < 2000; i++)
			assertTrue(RandomStringGenerator.getInstance().randomStringToken(13).length() == 13);
	}

}
