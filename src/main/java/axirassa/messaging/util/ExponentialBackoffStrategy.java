package axirassa.messaging.util;

/**
 * Implements a configurable exponential backoff strategy. See
 * {@link ExponentialBackoffStrategy#ExponentialBackoffStrategy(int, int, int)
 * the constuctor}
 * 
 * @author wiktor
 */
public class ExponentialBackoffStrategy implements BackoffStrategy {
	private int successTicks = 0;
	private int failureTicks = -1;

	private final int backoffActivationIndex;
	private final int minimumBackoff;
	private final int maximumBackoff;
	private final int increment;
	private final int successBackoffReset;


	/**
	 * @param minimumBackoff
	 *            the minimum backoff computed
	 * @param maximumBackoff
	 *            the maximum backoff that can be computed, set to 0 for no
	 *            limit
	 * @param backoffActivationIndex
	 *            the number of of failureTicks before a backoff is computed. set to 0
	 *            to immediately backoff
	 * @param increment
	 *            the basic amount to increment, once backoff is activated it
	 *            will follow the pattern X, 2X, 4X, 8X, 16X, etc.
	 * @param successBackoffReset
	 *            the amount of successful failureTicks it takes to reset the backoff
	 */
	public ExponentialBackoffStrategy(int minimumBackoff, int maximumBackoff, int backoffActivationIndex,
	        int increment, int successBackoffReset) {
		this.minimumBackoff = minimumBackoff;
		this.maximumBackoff = maximumBackoff;
		this.backoffActivationIndex = backoffActivationIndex;
		this.increment = increment;
		this.successBackoffReset = successBackoffReset;
	}
	
	@Override
    public void tickFailure() {
		failureTicks++;
		successTicks = 0;
	}


	@Override
	public void tickSuccess() {
		successTicks++;

		if (successTicks > successBackoffReset) {
			failureTicks = -1;
			successTicks = 0;
		}
	}


	@Override
    public long computeBackoffDelay() {
		long computedBackoff = 0;
		int effectiveTicks = failureTicks - backoffActivationIndex;
		
		if(effectiveTicks < 0)
			return 0;

		computedBackoff = increment * (long) Math.pow(2, effectiveTicks);

		if (maximumBackoff > 0 && computedBackoff > maximumBackoff)
			return maximumBackoff;

		if (minimumBackoff > computedBackoff)
			return minimumBackoff;

		return computedBackoff;
	}
}
