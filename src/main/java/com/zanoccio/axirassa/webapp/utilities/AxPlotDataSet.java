
package com.zanoccio.axirassa.webapp.utilities;

/**
 * A container for data for an individual dataset being plotted (typically with
 * multiple values for each measurement).
 * 
 * Note that the data is expected to be sorted on the x-axis.
 * 
 * @author wiktor
 * 
 */
public class AxPlotDataSet {

	private String label;
	private double[] axis;
	private double[][] data;
	private int datadepth = 0;

	private AxPlotRange ydatarange;
	private AxPlotRange yplotrange;

	private AxPlotRange xdatarange;
	private AxPlotRange xplotrange;


	public void setLabel(String label) {
		this.label = label;
	}


	public String getLabel() {
		return label;
	}


	public void setData(double[] axis, double[] data) {
		double[][] nested = new double[data.length][1];
		for (int i = 0; i < data.length; i++)
			nested[i][0] = data[i];

		setData(axis, nested);
	}


	public void setData(double[] axis, double[][] data) {
		datadepth = computeDataDepth(data);

		this.xdatarange = findXDataRange(axis);
		this.ydatarange = findYDataRange(data);

		this.axis = axis;
		this.data = data;
	}


	private int computeDataDepth(double[][] data) {
		int datadepth = 0;
		for (double[] column : data)
			if (column.length > datadepth)
				datadepth = column.length;

		return datadepth;
	}


	/**
	 * This makes the assumption that data is already pre-sorted on the x-axis.
	 */
	private AxPlotRange findXDataRange(double[] times) {
		if (times.length < 1)
			return new AxPlotRange(0, 0);

		return new AxPlotRange(times[0], times[times.length - 1]);
	}


	private AxPlotRange findYDataRange(double[][] data) {
		double min = 0;
		double max = 0;

		for (double[] column : data) {
			double total = 0;
			for (double value : column)
				total += value;

			if (total < min)
				min = total;

			if (total > max)
				max = total;
		}

		return new AxPlotRange(min, max);
	}


	public int getDataDepth() {
		return datadepth;
	}


	public double[] getAxis() {
		return axis;
	}


	public double[][] getData() {
		return data;
	}


	public void setYPlotRange(Double ymin, Double ymax) {
		setYPlotRange(new AxPlotRange(ymin, ymax));
	}


	public void setYPlotRange(AxPlotRange range) {
		yplotrange = range;
	}


	/**
	 * @return the desired plot range for the y-axis.
	 */
	public AxPlotRange getYPlotRange() {
		if (yplotrange != null)
			return yplotrange;

		return new AxPlotRange();
	}


	/**
	 * @return the actual minimum and maximum of the data on the y-axis.
	 *         (stacked, if appropriate)
	 */
	public AxPlotRange getYDataRange() {
		return ydatarange;
	}


	/**
	 * @see #setXRange(AxPlotRange)
	 */
	public void setXRange(Double xmin, Double xmax) {
		setXRange(new AxPlotRange(xmin, xmax));
	}


	/**
	 * Sets the plot range for the x-axis for this dataset
	 * 
	 * @param range
	 */
	public void setXRange(AxPlotRange range) {
		xplotrange = range;
	}


	/**
	 * @return the plot range for the x-axis. Returns an empty
	 *         {@link AxPlotRange} if a plot range hasn't been specified.
	 */
	public AxPlotRange getXRange() {
		if (xplotrange != null)
			return xplotrange;

		return new AxPlotRange();
	}

}
