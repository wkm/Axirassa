package axirassa.messaging.util;

/**
 * Implements a configurable exponential backoff strategy. See
 * {@link ExponentialBackoffStrategy#ExponentialBackoffStrategy(int, int, int)
 * the constuctor}
 * 
 * @author wiktor
 */
public class ExponentialBackoffStrategy {

	private int ticks = -1;
	private final int backoffActivationIndex;
	private final int minimumBackoff;
	private final int maximumBackoff;
	private final int increment;


	/**
	 * @param minimumBackoff
	 *            the minimum backoff computed
	 * @param maximumBackoff
	 *            the maximum backoff that can be computed, set to 0 for no
	 *            limit
	 * @param backoffActivationIndex
	 *            the number of of ticks before a backoff is computed. set to 0
	 *            to immediately backoff
	 * @param increment
	 *            the basic amount to increment, once backoff is activated it
	 *            will follow the pattern
	 */
	public ExponentialBackoffStrategy(int minimumBackoff, int maximumBackoff, int backoffActivationIndex, int increment) {
		this.minimumBackoff = minimumBackoff;
		this.maximumBackoff = maximumBackoff;
		this.backoffActivationIndex = backoffActivationIndex;
		this.increment = increment;
	}
	
	public void tickBackoff() {
		ticks++;
	}


	public long computeBackoffDelay() {
		long computedBackoff = 0;
		int effectiveTicks = ticks - backoffActivationIndex;
		
		if(effectiveTicks < 0)
			return 0;
		
		System.out.println("EFFECTIVE TICKS: " + effectiveTicks);

		computedBackoff = increment * (long) Math.pow(2, effectiveTicks);

		if (maximumBackoff > 0 && computedBackoff > maximumBackoff)
			return maximumBackoff;

		if (minimumBackoff > computedBackoff)
			return minimumBackoff;

		return computedBackoff;
	}
}
