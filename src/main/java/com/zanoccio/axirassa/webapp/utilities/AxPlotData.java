
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

	private int length;
	private List<String> labels;
	private Collection<Long> timestamps;
	private Double[][][] data;


	public void setLength(int length) {
		this.length = length;
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


	@Override
	public String toJSONString() {
		return toJSON().toCompactString();
	}


	public JSONObject toJSON() {
		JSONObject result = new JSONObject();

		result.put("length", length);
		result.put("labels", JSONConstructor.generateStrings(labels));
		result.put("times", JSONConstructor.generateLongs(timestamps));
		result.put("data", JSONConstructor.generate(data));

		return result;
	}
}