
package test.axirassa.messaging.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import axirassa.messaging.util.ExponentialBackoffStrategy;

public class TestExponentialBackoffStrategy {
	@Test
	public void simpleStrategy() {
		ExponentialBackoffStrategy strat = new ExponentialBackoffStrategy(10, 1000, 0, 100);

		assertEquals(0, strat.computeBackoffDelay());
		strat.tickBackoff();
		assertEquals(100, strat.computeBackoffDelay());
		strat.tickBackoff();
		assertEquals(200, strat.computeBackoffDelay());
		strat.tickBackoff();
		assertEquals(400, strat.computeBackoffDelay());
		strat.tickBackoff();
		assertEquals(800, strat.computeBackoffDelay());
		strat.tickBackoff();
		assertEquals(1000, strat.computeBackoffDelay());
	}


	@Test
	public void withActivation() {
		ExponentialBackoffStrategy strat = new ExponentialBackoffStrategy(200, 60000, 3, 1000);

		assertEquals(0, strat.computeBackoffDelay());
		strat.tickBackoff();
		assertEquals(0, strat.computeBackoffDelay());
		strat.tickBackoff();
		assertEquals(0, strat.computeBackoffDelay());
		strat.tickBackoff();
		assertEquals(0, strat.computeBackoffDelay());
		strat.tickBackoff();
		assertEquals(1000, strat.computeBackoffDelay());
		strat.tickBackoff();
		assertEquals(2000, strat.computeBackoffDelay());
		strat.tickBackoff();
		assertEquals(4000, strat.computeBackoffDelay());
		strat.tickBackoff();
		assertEquals(8000, strat.computeBackoffDelay());
		strat.tickBackoff();
		assertEquals(16000, strat.computeBackoffDelay());
		strat.tickBackoff();
		assertEquals(32000, strat.computeBackoffDelay());
		strat.tickBackoff();
		assertEquals(60000, strat.computeBackoffDelay());
		strat.tickBackoff();
	}
}
