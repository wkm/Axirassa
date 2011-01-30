
package axirassa.model;

import axirassa.webapp.utilities.LabeledObject;

public enum PingerFrequency implements LabeledObject {
	MINUTE(60),
	MINUTES_5(5 * 60),
	MINUTES_10(10 * 60),
	MINUTES_15(15 * 60),
	MINUTES_30(30 * 60),
	MINUTES_60(60 * 60);
	private int interval;


	PingerFrequency(int interval) {
		this.interval = interval;
	}


	/**
	 * @return the number of seconds in this interval
	 */
	public int getInterval() {
		return interval;
	}


	@Override
	public String getLabel() {
		if (interval == 1)
			return "every second";
		else if (interval < 60)
			return "every " + interval + " seconds";
		else if (interval == 60)
			return "every minute";
		else
			return "every " + (interval / 60) + " minutes";
	}
}
