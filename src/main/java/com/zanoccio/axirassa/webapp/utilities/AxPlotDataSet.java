
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
	private AxPlotRange yrange;
	private AxPlotRange xrange;


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
		for (double[] column : data)
			if (column.length > datadepth)
				datadepth = column.length;

		this.axis = axis;
		this.data = data;
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


	public void setYRange(Double ymin, Double ymax) {
		setYRange(new AxPlotRange(ymin, ymax));
	}


	public void setYRange(AxPlotRange range) {
		yrange = range;
	}


	public void setXRange(Double xmin, Double xmax) {
		setXRange(new AxPlotRange(xmin, xmax));
	}


	public void setXRange(AxPlotRange range) {
		xrange = range;
	}

}
