package com.zanoccio.axirassa.services.sentinel;

class DiskIOStatistic {
	String disk;
	long read;
	long written;
	private static final String DISKIO_STAT_INSERT = "INSERT INTO SentinelDiskIOStats VALUES (?,?,?,?,?)";
}