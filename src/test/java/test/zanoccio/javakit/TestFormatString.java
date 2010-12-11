
package test.zanoccio.javakit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import zanoccio.javakit.FormatString;


public class TestFormatString {
	@Test
	public void cutoff() {
		assertEquals("foo", FormatString.cutoff("foo", 3));
		assertEquals("foo", FormatString.cutoff("foo", 4));
		assertEquals("..", FormatString.cutoff("foo", 2));
		assertEquals("f..", FormatString.cutoff("foobar", 3));

		assertEquals("fo.", FormatString.cutoff("foobar", 3, "."));
		assertEquals("...", FormatString.cutoff("foobar", 3, "..."));
	}


	@Test
	public void center() {
		assertEquals("  foo  ", FormatString.center("foo", 7));
		assertEquals("..foo..", FormatString.center("foo", 7, '.'));
		assertEquals(" foo  ", FormatString.center("foo", 6));
		assertEquals("foobar", FormatString.center("foobar", 5));
		assertEquals("foobar", FormatString.center("foobar", 6));
	}
}
