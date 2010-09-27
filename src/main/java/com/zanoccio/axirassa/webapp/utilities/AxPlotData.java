
package com.zanoccio.axirassa.webapp.utilities;

import java.util.Collection;
import java.util.List;

import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.json.JSONString;

import com.zanoccio.axirassa.util.JSONConstructor;
import com.zanoccio.axirassa.webapp.components.AxPlot;

/**
 * Container for JSON data sent to an {@link AxPlot} component.
 * 
 * The {@link #toJSON()} method should be used to create the return value for
 * the event creating data for the component.
 * 
 * @author wiktor
 */
public class AxPlotData implements JSONString {

	/**
	 * @TODO the whole idea behind this enum is moronic --- or maybe it should
	 *       be an override. But fundamentally we want to set the type of the
	 *       dimension and let magic code handle the rest.
	 * 
	 * @author wiktor
	 */
	public enum AxPlotAxisLabelingFunction {
		PERCENT("ax.axp_axislab_percent"),
		DATA("ax.axp_axislab_data"),
		DATE("ax.axp_axislab_date");

		private String name;


		AxPlotAxisLabelingFunction(String name) {
			this.name = name;
		}


		@Override
		public String toString() {
			return name;
		}
	}


	private int length;
	private int datasets;
	private Double aggregatedmax;
	private List<String> labels;
	private Collection<Long> timestamps;
	private Double[][][] data;

	private AxPlotAxisLabelingFunction xaxis;
	private AxPlotAxisLabelingFunction yaxis;


	public AxPlotData(int length, int datasets, List<String> labels, Collection<Long> timestamps, Double[][][] data) {
		setLength(length);
		setDatasets(datasets);
		setLabels(labels);
		setTimestamps(timestamps);
		setData(data);
	}


	public void setLength(int length) {
		this.length = length;
	}


	public void setDatasets(int datasets) {
		this.datasets = datasets;
	}


	public void setAggregatedMax(double max) {
		this.aggregatedmax = max;
	}


	public void setLabels(List<String> labels) {
		this.labels = labels;
	}


	public void setTimestamps(Collection<Long> timestamps) {
		this.timestamps = timestamps;
	}


	public void setData(Double[][][] data) {
		this.data = data;
	}


	public void setXAxisLabelingFunction(AxPlotAxisLabelingFunction xaxis) {
		this.xaxis = xaxis;
	}


	public void setYAxisLabelingFunction(AxPlotAxisLabelingFunction yaxis) {
		this.yaxis = yaxis;
	}


	@Override
	public String toJSONString() {
		return toJSON().toCompactString();
	}


	public JSONObject toJSON() {
		JSONObject result = new JSONObject();

		result.put("length", length);
		result.put("datasets", datasets);
		result.put("aggregatedMax", aggregatedmax);
		if (xaxis != null)
			result.put("xaxislabelfn", xaxis.toString());
		if (yaxis != null)
			result.put("yaxislabelfn", yaxis.toString());
		result.put("labels", JSONConstructor.generateStrings(labels));
		result.put("times", JSONConstructor.generateLongs(timestamps));
		result.put("data", JSONConstructor.generate(data));

		return result;
	}
}