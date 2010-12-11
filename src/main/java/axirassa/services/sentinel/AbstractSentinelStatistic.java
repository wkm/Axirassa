
package axirassa.services.sentinel;

import java.util.Date;

public abstract class AbstractSentinelStatistic implements SentinelStatistic {

	private final int machineid;
	private final Date timestamp;


	public AbstractSentinelStatistic(int machineid, Date timestamp) {
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
