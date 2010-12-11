
package test.axirassa.util;

import org.junit.Test;

import axirassa.util.JSONConstructor;

public class TestJSONConstructor {
	@Test
	public void logspeed() {
		System.out.println(JSONConstructor.format(12.1231, 2, 1));
	}
}
