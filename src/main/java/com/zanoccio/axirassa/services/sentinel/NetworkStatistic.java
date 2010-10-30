package com.zanoccio.axirassa.services.sentinel;

class NetworkStatistic {
	String device;
	long send;
	long receive;
	static final String NETWORK_STAT_INSERT = "INSERT INTO SentinelNetworkStats VALUES (?,?,?,?,?)";
}