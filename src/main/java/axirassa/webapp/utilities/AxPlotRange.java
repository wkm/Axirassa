
package axirassa.webapp.utilities;

import org.apache.tapestry5.json.JSONLiteral;

public class AxPlotRange {
	private final Double min;
	private final Double max;


	/**
	 * Represents an "automatic" plot range with no explicit min or max setting.
	 */
	public AxPlotRange() {
		min = null;
		max = null;
	}


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


	public JSONLiteral toJSON() {
		StringBuilder sb = new StringBuilder(50);
		sb.append('[');
		sb.append(min);
		sb.append(',');
		sb.append(max);
		sb.append(']');

		return new JSONLiteral(sb.toString());
	}
}
