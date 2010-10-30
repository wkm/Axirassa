
package com.zanoccio.axirassa.services.sentinel;

class NetworkThroughputStatistic {
	private static final String THROUGHPUT_STAT_INSERT = "INSERT INTO SentinelThroughputStats VALUES (?,?,?,?,?)";

	String device;
	float send;
	float receive;
}