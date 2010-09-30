
package com.zanoccio.axirassa.webapp.utilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.apache.tapestry5.json.JSONLiteral;
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
 * Note on terminology: since we're dealing with 3D arrays, we refer to "rows"
 * (x-axis); "columns" (y-axis); and "bars" (z-axis).
 * 
 * @author wiktor
 */
public class AxPlotDataPackage implements JSONString {

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

		private final String name;


		AxPlotAxisLabelingFunction(String name) {
			this.name = name;
		}


		public JSONLiteral toJSON() {
			return new JSONLiteral(name);
		}
	}


	private final ArrayList<AxPlotDataSet> datasets = new ArrayList<AxPlotDataSet>();

	private AxPlotAxisLabelingFunction xaxis;
	private AxPlotAxisLabelingFunction yaxis;

	private AxPlotRange xaxisrange;
	private AxPlotRange yaxisrange;

	private boolean labelDataSets = true;


	public void addDataSet(AxPlotDataSet dataset) {
		datasets.add(dataset);
	}


	public List<AxPlotDataSet> getDataSets() {
		return datasets;
	}


	public void setXAxisLabelingFunction(AxPlotAxisLabelingFunction xaxis) {
		this.xaxis = xaxis;
	}


	public void setYAxisLabelingFunction(AxPlotAxisLabelingFunction yaxis) {
		this.yaxis = yaxis;
	}


	public void setAggregatedYAxisRange(AxPlotRange range) {
		this.yaxisrange = range;
	}


	public void setAggregatedXAxisRange(AxPlotRange range) {
		this.xaxisrange = range;
	}


	public void setLabelDataSets(boolean value) {
		labelDataSets = value;
	}


	private TreeSet<Double> axisvalues;
	private Double[][][] data;
	private int datadepth;


	private void constructAlignedTable() {
		// compute all unique axis values across the domain
		axisvalues = new TreeSet<Double>();
		datadepth = 0;
		for (AxPlotDataSet dataset : datasets) {
			if (datadepth < dataset.getDataDepth())
				datadepth = dataset.getDataDepth();

			for (double value : dataset.getAxis())
				axisvalues.add(value);
		}

		// allocate a table for the values
		data = new Double[datasets.size()][axisvalues.size()][datadepth];

		// copy data from each dataset into the the table
		int datasetindex = 0;
		for (AxPlotDataSet dataset : datasets) {
			Iterator<Double> axisiterator = axisvalues.iterator();

			double[] datasetaxis = dataset.getAxis();
			double[][] datasetdata = dataset.getData();

			int columncursor = 0;
			for (int i = 0; i < datasetdata.length; i++) {

				// skip over any axis point for which we don't have data
				while (axisiterator.next() < datasetaxis[i]) {
					// mark each point in this bar of the table as null
					for (int j = 0; j < datadepth; j++)
						data[datasetindex][columncursor][j] = null;

					// move forward in the tableset
					columncursor++;
				}

				// copy over this bar of data
				for (int j = 0; j < datadepth; j++)
					if (j < datasetdata[i].length)
						// values where we have them
						data[datasetindex][columncursor][j] = datasetdata[i][j];
					else
						// nulls for the rest
						data[datasetindex][columncursor][j] = null;

				columncursor++;
			}

			datasetindex++;
		}
	}


	@Override
	public String toJSONString() {
		return toJSON().toCompactString();
	}


	public JSONObject toJSON() {
		constructAlignedTable();

		JSONObject result = new JSONObject();

		ArrayList<String> labels = new ArrayList<String>(datasets.size());
		for (AxPlotDataSet dataset : datasets)
			labels.add(dataset.getLabel());

		result.put("labelDataSets", labelDataSets);
		result.put("depth", datadepth);
		result.put("datasets", datasets.size());
		result.put("aggregatedMin", yaxisrange.getMin());
		result.put("aggregatedMax", yaxisrange.getMax());
		if (xaxis != null)
			result.put("xaxislabelfn", xaxis.toJSON());
		if (yaxis != null)
			result.put("yaxislabelfn", yaxis.toJSON());
		result.put("labels", JSONConstructor.generateStrings(labels));
		result.put("times", JSONConstructor.generate(axisvalues));
		result.put("data", JSONConstructor.generate(data));

		return result;
	}
}