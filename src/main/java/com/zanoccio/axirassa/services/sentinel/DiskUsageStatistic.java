
package com.zanoccio.axirassa.services.sentinel;

import java.util.Date;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

class DiskUsageStatistic extends AbstractSentinelStatistic {
	private static final String DISKUSAGE_STAT_INSERT = "INSERT INTO SentinelDiskUsageStats VALUES (?,?,?,?,?)";

	private final String disk;
	private final long used;
	private final long total;


	public DiskUsageStatistic(int machineid, Date timestamp, String disk, long used, long total) {
		super(machineid, timestamp);

		this.disk = disk;
		this.used = used;
		this.total = total;
	}


	@Override
	public void save(Session session) {
		SQLQuery query = session.createSQLQuery(DISKUSAGE_STAT_INSERT);

		query.setInteger(0, getMachineID());
		query.setTimestamp(1, getTimestamp());
		query.setString(2, disk);
		query.setLong(3, used);
		query.setLong(4, total);

		query.executeUpdate();
	}
}
