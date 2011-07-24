
package test.axirassa.pinger;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import axirassa.services.pinger.BandwidthThreadAllocator;
import axirassa.services.pinger.BandwidthThreadAllocator.BandwidthThreadAllocatorException;

public class TestBandwidthThreadAllocator {
	@Test
	public void test() throws BandwidthThreadAllocatorException {
		BandwidthThreadAllocator alloc = new BandwidthThreadAllocator(5, 500, 5, 50000);
		
		assertEquals(5, alloc.getMinThreads());
		assertEquals(500, alloc.getMaxThreads());
		assertEquals(5, alloc.getInitialThreads());
		assertEquals(50000, alloc.getTargetRate());
		
		
		alloc.addBandwidthMeasurement(1);
		
		assertEquals(500, alloc.getTargetThreadCount());
		assertEquals(495, alloc.getTargetThreadCountDelta());
		alloc.applyThreadCountDelta(alloc.getTargetThreadCountDelta());
		
		assertEquals(500, alloc.getCurrentThreads());
		
		
		alloc.addBandwidthMeasurement(500000);
		assertEquals(50, alloc.getTargetThreadCount());
		assertEquals(-450, alloc.getTargetThreadCountDelta());
		alloc.applyThreadCountDelta(alloc.getTargetThreadCountDelta());
	}
}
