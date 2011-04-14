
package axirassa.overlord;

import lombok.Getter;

/**
 * Represents a period of time over which the number of restarts of a process
 * may be watched.
 * 
 * @author wiktor
 * 
 */
public enum MonitorPeriod {
	MINUTE(60 * 1000),
	HOUR(60 * 60 * 1000),
	DAY(24 * 60 * 60 * 1000);

	@Getter
	private final long millis;


	MonitorPeriod (long millis) {
		this.millis = millis;
	}
}
