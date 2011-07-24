
package axirassa.services.pinger;

import java.util.LinkedList;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * A class for deciding the number of threads to allocate for pinging from a
 * history of thread/bandwidth measurements
 * 
 * @author wiktor
 * 
 */
@Slf4j
public class BandwidthThreadAllocator {
	public class BandwidthThreadAllocatorException extends Exception {
		private static final long serialVersionUID = -4160650705103431002L;


		public BandwidthThreadAllocatorException(String string) {
			super(string);
		}
	}


	private static final int HISTORY_SIZE = 50;

	@Getter
	private final int minThreads;

	@Getter
	private final int maxThreads;

	@Getter
	private final int initialThreads;

	@Getter
	@Setter
	private long targetRate;

	@Getter
	@Setter
	private int currentThreads;

	private final LinkedList<BandwidthMeasurement> measurements = new LinkedList<BandwidthMeasurement>();


	public BandwidthThreadAllocator(int minThreads, int maxThreads, int initialThreads, long targetRate) {
		this.minThreads = minThreads;
		this.maxThreads = maxThreads;
		this.initialThreads = initialThreads;
		this.targetRate = targetRate;

		this.currentThreads = initialThreads;
	}


	public void addBandwidthMeasurement(long rate) {
		BandwidthMeasurement measurement = new BandwidthMeasurement();
		measurement.bandwidth = rate;
		measurement.threadCount = currentThreads;
		measurements.add(measurement);

		if (measurements.size() > HISTORY_SIZE)
			measurements.pop();
	}


	public int getTargetThreadCount() {
		int target = 0;

		BandwidthMeasurement lastMeasurement = measurements.peekLast();
		double perThreadRate = ((double) lastMeasurement.bandwidth) / lastMeasurement.threadCount;
		target = (int) Math.round(targetRate / perThreadRate);

		log.trace("Threads: {}  Rate: {}  Rate/Thread: {}  TargetRate: {}  Target: {}", new Object[] {
		        lastMeasurement.threadCount, lastMeasurement.bandwidth, perThreadRate, targetRate, target });

		if (measurements.size() <= 0)
			target = initialThreads;

		if (target < minThreads)
			target = minThreads;

		if (target > maxThreads)
			target = maxThreads;

		return target;
	}


	public int getTargetThreadCountDelta() {
		return getTargetThreadCount() - currentThreads;
	}


	public void applyThreadCountDelta(int count) throws BandwidthThreadAllocatorException {
		currentThreads += count;

		if (currentThreads < 1)
			throw new BandwidthThreadAllocatorException("Current thread count is below 1");

		if (currentThreads < minThreads)
			throw new BandwidthThreadAllocatorException("Current thread count " + currentThreads
			        + " is below minimum thread count " + minThreads);

		if (currentThreads > maxThreads)
			throw new BandwidthThreadAllocatorException("Current thread count " + currentThreads
			        + " is above maximum thread count " + maxThreads);
	}
}

class BandwidthMeasurement {
	int threadCount;
	long bandwidth;
}
