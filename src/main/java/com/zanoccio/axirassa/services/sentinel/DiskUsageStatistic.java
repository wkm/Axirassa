package com.zanoccio.axirassa.services.sentinel;

class DiskUsageStatistic {
	String disk;
	long used;
	long total;
	static final String DISKUSAGE_STAT_INSERT = "INSERT INTO SentinelDiskUsageStats VALUES (?,?,?,?,?)";
}