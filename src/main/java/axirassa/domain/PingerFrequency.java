
package axirassa.domain;

import axirassa.webapp.utilities.LabeledObject;

public enum PingerFrequency implements LabeledObject {
	MINUTE(1),
	MINUTES_5(5),
	MINUTES_10(10),
	MINUTES_15(15),
	MINUTES_30(30),
	MINUTES_60(60);
	private int interval;


	PingerFrequency(int interval) {
		this.interval = interval;
	}


	public int getInterval() {
		return interval;
	}


	@Override
	public String getLabel() {
		if (interval == 1)
			return "minute";
		else
			return interval + " minutes";
	}
}
