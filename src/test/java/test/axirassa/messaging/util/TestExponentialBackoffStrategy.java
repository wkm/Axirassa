
package test.axirassa.messaging.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import axirassa.messaging.util.BackoffStrategy;
import axirassa.messaging.util.ExponentialBackoffStrategy;

public class TestExponentialBackoffStrategy {
	@Test
	public void simpleStrategy() {
		BackoffStrategy strat = new ExponentialBackoffStrategy(10, 1000, 0, 100, 0);

		// no backoff when all is well
		for (int i = 0; i < 100; i++) {
			strat.tickSuccess();
			assertEquals(0, strat.computeBackoffDelay());
		}

		assertEquals(0, strat.computeBackoffDelay());
		strat.tickFailure();
		assertEquals(100, strat.computeBackoffDelay());
		strat.tickFailure();
		assertEquals(200, strat.computeBackoffDelay());
		strat.tickFailure();
		assertEquals(400, strat.computeBackoffDelay());
		strat.tickFailure();
		assertEquals(800, strat.computeBackoffDelay());
		strat.tickFailure();
		assertEquals(1000, strat.computeBackoffDelay());
		strat.tickFailure();
		assertEquals(1000, strat.computeBackoffDelay());
		strat.tickFailure();
		assertEquals(1000, strat.computeBackoffDelay());

		// immediately reset backoff on success
		strat.tickSuccess();
		assertEquals(0, strat.computeBackoffDelay());

		strat.tickFailure();
		assertEquals(100, strat.computeBackoffDelay());
		strat.tickFailure();
		assertEquals(200, strat.computeBackoffDelay());
	}


	@Test
	public void withActivation() {
		BackoffStrategy strat = new ExponentialBackoffStrategy(200, 60000, 3, 1000, 0);

		assertEquals(0, strat.computeBackoffDelay());
		strat.tickFailure();
		assertEquals(0, strat.computeBackoffDelay());
		strat.tickFailure();
		assertEquals(0, strat.computeBackoffDelay());
		strat.tickFailure();
		assertEquals(0, strat.computeBackoffDelay());
		strat.tickFailure();
		assertEquals(1000, strat.computeBackoffDelay());
		strat.tickFailure();
		assertEquals(2000, strat.computeBackoffDelay());
		strat.tickFailure();
		assertEquals(4000, strat.computeBackoffDelay());
		strat.tickFailure();
		assertEquals(8000, strat.computeBackoffDelay());
		strat.tickFailure();
		assertEquals(16000, strat.computeBackoffDelay());
		strat.tickFailure();
		assertEquals(32000, strat.computeBackoffDelay());
		strat.tickFailure();
		assertEquals(60000, strat.computeBackoffDelay());
		strat.tickFailure();

		strat.tickSuccess();
		assertEquals(0, strat.computeBackoffDelay());
		strat.tickSuccess();
		assertEquals(0, strat.computeBackoffDelay());
		strat.tickFailure();
		assertEquals(0, strat.computeBackoffDelay());
		strat.tickFailure();
		assertEquals(0, strat.computeBackoffDelay());
		strat.tickFailure();
		assertEquals(0, strat.computeBackoffDelay());
		strat.tickFailure();
		assertEquals(1000, strat.computeBackoffDelay());
		strat.tickFailure();
	}


	@Test
	public void withDelayedSuccessReset() {
		BackoffStrategy strat = new ExponentialBackoffStrategy(200, 60000, 3, 1000, 3);

		assertEquals(0, strat.computeBackoffDelay());
		strat.tickFailure();
		assertEquals(0, strat.computeBackoffDelay());
		strat.tickFailure();
		assertEquals(0, strat.computeBackoffDelay());
		strat.tickFailure();
		assertEquals(0, strat.computeBackoffDelay());
		strat.tickFailure();
		assertEquals(1000, strat.computeBackoffDelay());
		strat.tickFailure();
		assertEquals(2000, strat.computeBackoffDelay());
		strat.tickFailure();
		assertEquals(4000, strat.computeBackoffDelay());
		strat.tickFailure();
		assertEquals(8000, strat.computeBackoffDelay());
		strat.tickFailure();
		assertEquals(16000, strat.computeBackoffDelay());
		strat.tickFailure();
		assertEquals(32000, strat.computeBackoffDelay());
		strat.tickFailure();
		assertEquals(60000, strat.computeBackoffDelay());
		strat.tickFailure();

		// three successes until the backoff is reset
		strat.tickSuccess();
		assertEquals(60000, strat.computeBackoffDelay());
		strat.tickSuccess();
		assertEquals(60000, strat.computeBackoffDelay());
		strat.tickSuccess();
		assertEquals(60000, strat.computeBackoffDelay());
		strat.tickSuccess();
		assertEquals(0, strat.computeBackoffDelay());

		// and try failing again
		strat.tickFailure();
		assertEquals(0, strat.computeBackoffDelay());
		strat.tickFailure();
		assertEquals(0, strat.computeBackoffDelay());
		strat.tickFailure();
		assertEquals(0, strat.computeBackoffDelay());
		strat.tickFailure();
		assertEquals(1000, strat.computeBackoffDelay());
		strat.tickFailure();
	}
}
