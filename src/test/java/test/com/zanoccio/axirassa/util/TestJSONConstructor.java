
package test.com.zanoccio.axirassa.util;

import org.junit.Test;

import com.zanoccio.axirassa.util.JSONConstructor;

public class TestJSONConstructor {
	@Test
	public void logspeed() {
		System.out.println(JSONConstructor.format(12.1231, 2, 1));
	}
}
