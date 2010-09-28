
package com.zanoccio.axirassa.webapp.utilities;

public class AxPlotRange {
	private final Double min;
	private final Double max;


	public AxPlotRange(Double min, Double max) {
		this.min = min;
		this.max = max;
	}


	public AxPlotRange(int min, int max) {
		this.min = (double) min;
		this.max = (double) max;
	}


	public AxPlotRange(long min, long max) {
		this.min = (double) min;
		this.max = (double) max;
	}


	public Double getMax() {
		return max;
	}


	public Double getMin() {
		return min;
	}
}
