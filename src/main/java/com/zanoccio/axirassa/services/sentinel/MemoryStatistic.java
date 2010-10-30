package com.zanoccio.axirassa.services.sentinel;

class MemoryStatistic {
	long used;
	long total;
	static final String MEMORY_STAT_INSERT = "INSERT INTO SentinelMemoryStats VALUES (?,?,?,?)";
}