
package test.zanoccio.javakit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import zanoccio.javakit.StringUtilities;

public class TestStringUtilities {
	@Test
	public void removeLeadingWhitespace() {
		assertEquals("hi\nthere\n!", StringUtilities.removeLeadingWhitespace("\t hi\n\t there\n\t   !"));
		assertEquals("hi\n\nthere!", StringUtilities.removeLeadingWhitespace("\thi\n\t\n\tthere!\n"));
	}
}
