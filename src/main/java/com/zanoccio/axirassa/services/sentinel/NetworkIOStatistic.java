
package com.zanoccio.axirassa.services.sentinel;

import java.util.Date;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

class NetworkIOStatistic extends AbstractSentinelStatistic {

	private static final String THROUGHPUT_STAT_INSERT = "INSERT INTO SentinelNetworkIOStats VALUES (?,?,?,?,?)";

	private final String device;
	private final long rxrate;
	private final long txrate;


	public NetworkIOStatistic(int machineid, Date timestamp, String device, long rxrate, long txrate) {
		super(machineid, timestamp);

		this.device = device;
		this.rxrate = rxrate;
		this.txrate = txrate;
	}


	@Override
	public void save(Session session) {
		SQLQuery query = session.createSQLQuery(THROUGHPUT_STAT_INSERT);

		query.setInteger(0, getMachineID());
		query.setTimestamp(1, getTimestamp());
		query.setString(2, device);
		query.setLong(3, rxrate);
		query.setLong(4, txrate);

		query.executeUpdate();
	}

}
