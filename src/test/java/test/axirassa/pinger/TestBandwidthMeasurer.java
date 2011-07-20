package test.axirassa.pinger;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import axirassa.services.pinger.BandwidthMeasurer;

public class TestBandwidthMeasurer {
	@Test
	public void test() {
		BandwidthMeasurer measurer = new BandwidthMeasurer(100);

		measurer.addDownload(10, 10, 5);
		assertEquals(10, measurer.currentRate(100));
		assertEquals(10, measurer.currentRate(100));

		measurer.addDownload(10, 15, 5);
		assertEquals(20, measurer.currentRate(100));

		measurer.addDownload(100, 100, 100);
		assertEquals(120, measurer.currentRate(100));
		assertEquals(50, measurer.currentRate(150));

		measurer.addDownload(50, 100, 50);
		assertEquals(100, measurer.currentRate(150));

		assertEquals(0, measurer.currentRate(200));
	}
}
