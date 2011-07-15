
package axirassa.messaging.util;

public interface BackoffStrategy {

	/**
	 * register a failure occurrence in the strategy (usually causing some
	 * backoff to be triggered)
	 */
	public void tickFailure();


	/**
	 * register a successful occurrence in the strategy
	 */
	public void tickSuccess();


	/**
	 * @return the backoff delay as computed by the strategy; you should be
	 *         prepared for a delay of 0, perhaps with special handling in the
	 *         common, correct case
	 */
	public long computeBackoffDelay();

}
