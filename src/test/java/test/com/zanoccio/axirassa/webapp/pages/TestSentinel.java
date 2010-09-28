
package test.com.zanoccio.axirassa.webapp.pages;

import org.apache.tapestry5.test.PageTester;
import org.junit.Test;

public class TestSentinel {
	@Test
	public void testCPUUpdate() {
		PageTester tester = new PageTester("com.zanoccio.axirassa.webapp", "", "src/main/java");
		tester.renderPage("Sentinel.cpuupdate");
	}
}
