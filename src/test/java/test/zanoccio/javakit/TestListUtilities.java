package test.zanoccio.javakit;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import zanoccio.javakit.ListUtilities;

public class TestListUtilities {
	@Test
	public void padRight() {
		assertEquals(0, ListUtilities.padRight(new ArrayList<String>(), 0, "Hi").size());
		assertEquals(1, ListUtilities.padRight(new ArrayList<String>(), 1, "Hi").size());
		assertEquals(5, ListUtilities.padRight(new ArrayList<String>(), 5, "Hi").size());
		assertEquals(10, ListUtilities.padRight(new ArrayList<String>(), 10, "Hi").size());
	}
}
