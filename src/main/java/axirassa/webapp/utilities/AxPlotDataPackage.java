
package axirassa.webapp.utilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONLiteral;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.json.JSONString;

import axirassa.util.JSONConstructor;
import axirassa.webapp.components.AxPlot;


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


	private static final int JSON_BUFFER_SIZE = 4096;

	private final ArrayList<AxPlotDataSet> datasets = new ArrayList<AxPlotDataSet>();

	private AxPlotAxisLabelingFunction xaxis;
	private AxPlotAxisLabelingFunction yaxis;

	private String dataFormat = "%f";
	private String axisFormat = "%f";

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


	public void setDataFormat(String format) {
		dataFormat = format;
	}


	public String getDataFormat() {
		return dataFormat;
	}


	public void setAxisFormat(String format) {
		axisFormat = format;
	}


	public String getAxisFormat() {
		return axisFormat;
	}


	private TreeSet<Double> axisvalues;
	private String datajson;
	private int datadepth;


	private void constructAlignedTable() {
		StringBuilder json = new StringBuilder(JSON_BUFFER_SIZE);

		// compute all unique axis values across the domain
		axisvalues = new TreeSet<Double>();
		datadepth = 0;
		for (AxPlotDataSet dataset : datasets) {
			if (datadepth < dataset.getDataDepth())
				datadepth = dataset.getDataDepth();

			for (double value : dataset.getAxis())
				axisvalues.add(value);
		}

		json.append('[');

		// copy data from each dataset into the the table
		int datasetindex = 0;
		for (AxPlotDataSet dataset : datasets) {
			Iterator<Double> axisiterator = axisvalues.iterator();

			double[] datasetaxis = dataset.getAxis();
			double[][] datasetdata = dataset.getData();

			int columncursor = 0;
			json.append('[');
			for (int i = 0; i < datasetdata.length; i++) {

				// skip over any axis point for which we don't have data
				while (axisiterator.next() < datasetaxis[i]) {
					json.append('[');
					// mark each point in this bar of the table as null
					for (int j = 0; j < datadepth; j++) {
						json.append("null");

						if (j < datadepth - 1)
							json.append(',');
					}
					json.append("],");

					// move forward in the tableset
					columncursor++;
				}

				// copy over this bar of data
				json.append('[');
				for (int j = 0; j < datadepth; j++) {
					if (j < datasetdata[i].length)
						if (datasetdata[i][j] == 0.0)
							json.append('0');
						else
							// values where we have them
							json.append(datasetdata[i][j]);
					else
						json.append("null");

					if (j < datadepth - 1)
						json.append(',');
				}
				json.append(']');

				if (i < datasetdata.length - 1)
					json.append(',');

				columncursor++;
			}

			// fill out any remaining axis points with nulls
			while (axisiterator.hasNext()) {
				axisiterator.next();

				json.append(',');

				json.append('[');
				for (int i = 0; i < datadepth; i++) {
					json.append("null");

					if (i < datadepth - 1)
						json.append(',');
				}
				json.append(']');

			}

			json.append(']');

			if (datasetindex < datasets.size() - 1)
				json.append(',');

			datasetindex++;
		}

		json.append(']');

		datajson = json.toString();
	}


	@Override
	public String toJSONString() {
		return toJSON().toCompactString();
	}


	public JSONObject toJSON() {
		constructAlignedTable();

		JSONObject result = new JSONObject();

		// extract labels and plot ranges
		ArrayList<String> labels = new ArrayList<String>(datasets.size());
		JSONArray plotranges = new JSONArray();
		StringBuilder sb = new StringBuilder();
		for (AxPlotDataSet dataset : datasets) {
			labels.add(dataset.getLabel());

			AxPlotRange xrange = dataset.getXRange();
			AxPlotRange yrange = dataset.getYPlotRange();

			plotranges.put(new JSONArray(xrange.toJSON(), yrange.toJSON()));
		}

		result.put("labelDataSets", labelDataSets);
		result.put("depth", datadepth);
		result.put("datasets", datasets.size());
		result.put("plotranges", plotranges);
		if (yaxisrange != null)
			result.put("aggregatedMin", yaxisrange.getMin());
		if (yaxisrange != null)
			result.put("aggregatedMax", yaxisrange.getMax());
		if (xaxis != null)
			result.put("xaxislabelfn", xaxis.toJSON());
		if (yaxis != null)
			result.put("yaxislabelfn", yaxis.toJSON());
		result.put("labels", JSONConstructor.generateStrings(labels));
		result.put("times", JSONConstructor.generate(axisvalues));
		result.put("data", new JSONLiteral(datajson));

		return result;
	}
}
