
package com.zanoccio.axirassa.services.sentinel;

class NetworkThroughputStatistic {
	String device;
	float send;
	float receive;
	private static final String THROUGHPUT_STAT_INSERT = "INSERT INTO SentinelThroughputStats VALUES (?,?,?,?,?)";
}