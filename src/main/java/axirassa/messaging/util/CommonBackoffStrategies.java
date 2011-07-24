
package axirassa.messaging.util;

public class CommonBackoffStrategies {
	public final static ExponentialBackoffStrategy EXPONENTIAL_BACKOFF_MESSAGING() {
		return new ExponentialBackoffStrategy(0, 60000, 3, 1000, 60);
	}
}
