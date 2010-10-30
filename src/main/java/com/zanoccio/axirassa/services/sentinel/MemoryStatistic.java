
package com.zanoccio.axirassa.services.sentinel;

import java.util.Date;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

class MemoryStatistic extends AbstractSentinelStatistic {
	private static final String MEMORY_STAT_INSERT = "INSERT INTO SentinelMemoryStats VALUES (?,?,?,?)";

	private final long used;
	private final long total;


	public MemoryStatistic(int machineid, Date timestamp, long used, long total) {
		super(machineid, timestamp);

		this.used = used;
		this.total = total;
	}


	@Override
	public void save(Session session) {
		SQLQuery query = session.createSQLQuery(MEMORY_STAT_INSERT);

		query.setInteger(0, getMachineID());
		query.setTimestamp(1, getTimestamp());
		query.setLong(2, used);
		query.setLong(3, total);

		query.executeUpdate();
	}
}