
package test.zanoccio.javakit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import zanoccio.javakit.ArrayUtilities;

public class TestArrayUtilities {
	@Test
	public void toStringByteArray() {
		assertEquals("", ArrayUtilities.toString(new byte[] {}));

		assertEquals("104-101-108-108-111", ArrayUtilities.toString("hello".getBytes()));
		assertEquals("125-126-127-0-128-129-130", ArrayUtilities.toString(new byte[] { 125, 126, 127, 0, -1, -2, -3 }));
	}
}
