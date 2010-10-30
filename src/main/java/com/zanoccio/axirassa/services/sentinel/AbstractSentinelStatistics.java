
package com.zanoccio.axirassa.services.sentinel;

import java.util.Date;

public abstract class AbstractSentinelStatistics implements SentinelStatistic {

	private final int machineid;
	private final Date timestamp;


	public AbstractSentinelStatistics(int machineid, Date timestamp) {
		this.machineid = machineid;
		this.timestamp = timestamp;
	}


	public int getMachineID() {
		return machineid;
	}


	public Date getTimestamp() {
		return timestamp;
	}

}
