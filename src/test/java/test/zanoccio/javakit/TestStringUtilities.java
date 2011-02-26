
package test.zanoccio.javakit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import zanoccio.javakit.StringUtilities;

public class TestStringUtilities {
	@Test
	public void removeLeadingWhitespace() {
		assertEquals("hi\nthere\n!", StringUtilities.removeLeadingWhitespace("\t hi\n\t there\n\t   !"));
	}
}
