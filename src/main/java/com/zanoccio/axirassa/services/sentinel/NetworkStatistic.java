
package com.zanoccio.axirassa.services.sentinel;

import java.util.Date;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

class NetworkStatistic extends AbstractSentinelStatistic {
	private static final String NETWORK_STAT_INSERT = "INSERT INTO SentinelNetworkStats VALUES (?,?,?,?,?)";

	private final String device;
	private final long send;
	private final long receive;


	public NetworkStatistic(int machineid, Date timestamp, String device, long send, long receive) {
		super(machineid, timestamp);

		this.device = device;
		this.send = send;
		this.receive = receive;
	}


	@Override
	public void save(Session session) {
		SQLQuery query = session.createSQLQuery(NETWORK_STAT_INSERT);

		query.setInteger(0, getMachineID());
		query.setTimestamp(1, getTimestamp());
		query.setString(2, device);
		query.setLong(3, send);
		query.setLong(4, receive);

		query.executeUpdate();
	}
}