
package axirassa.services.pinger;

import java.util.Iterator;
import java.util.LinkedList;

public class BandwidthMeasurer {
	private final int windowSize;


	public BandwidthMeasurer(int windowSize) {
		this.windowSize = windowSize;
	}


	private final LinkedList<BandwidthUsageEntry> usages = new LinkedList<BandwidthUsageEntry>();


	private void setMsTick(long currentMs) {
		long tickThreshold = currentMs - windowSize;

		Iterator<BandwidthUsageEntry> entryIterator = usages.iterator();
		while (entryIterator.hasNext()) {
			BandwidthUsageEntry entry = entryIterator.next();

			if (entry.stopTick < tickThreshold)
				entryIterator.remove();
			else
				break;
		}
	}


	/**
	 * Adds a download entry for bandwidth logging. <tt>currentMs</tt> must be
	 * greater than or equal to all previous calls to {@link #addDownload}
	 */
	public void addDownload(long bytes, int currentMs, int msConsumed) {
		setMsTick(currentMs);

		BandwidthUsageEntry entry = new BandwidthUsageEntry();

		entry.startTick = currentMs - msConsumed;
		entry.stopTick = currentMs;
		entry.bytes = bytes;

		usages.addLast(entry);
	}


	/**
	 * computes the current bandwidth rate in bytes/second for the default
	 * window size
	 */
	public long currentRate(long currentMs) {
		return currentRate(windowSize, currentMs);
	}


	/**
	 * computes the current bandwidth rate in bytes/second for the specified
	 * window size (which must be equal to or less than the default window size)
	 */
	public long currentRate(int windowSize, long currentMs) {
		setMsTick(currentMs);

		long windowStart = currentMs - windowSize;
		long rate = 0;

		Iterator<BandwidthUsageEntry> entryIterator = usages.iterator();
		while (entryIterator.hasNext()) {
			BandwidthUsageEntry entry = entryIterator.next();

			if (entry.stopTick <= windowStart)
				continue;

			rate += computeEntryAddition(windowStart, entry);
		}

		return rate;
	}


	private long computeEntryAddition(long windowStart, BandwidthUsageEntry entry) {
		if (windowStart <= entry.startTick)
			return entry.bytes;

		double entryDuration = entry.stopTick - entry.startTick;
		double entryRatio = (entry.stopTick - windowStart) / entryDuration;

		return (long) (entry.bytes * entryRatio);
	}
}

class BandwidthUsageEntry {
	int startTick;
	int stopTick;

	long bytes;


	@Override
	public String toString() {
		return String.format("BandwidthUsage [start: %d, stop: %d, bytes: %d]", startTick, stopTick, bytes);
	}
}
