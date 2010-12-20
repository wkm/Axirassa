
package test.axirassa.webapp.pages;

import org.junit.Test;

import axirassa.util.HibernateTools;
import axirassa.webapp.pages.Sentinel;

public class TestSentinel {
	@Test
	public void testCPUUpdate() {
		HibernateTools.getSession().disconnect();

		Sentinel sent = new Sentinel();
		int i = 5;
		do {
			long start = System.currentTimeMillis();
			sent.onActionFromCpuupdate("24h");
			System.out.println("time: " + (System.currentTimeMillis() - start));
		} while (i-- > 0);
	}
}
