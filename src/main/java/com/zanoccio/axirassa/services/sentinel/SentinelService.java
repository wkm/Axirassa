
package com.zanoccio.axirassa.services.sentinel;

import java.util.ArrayList;
import java.util.Date;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import com.zanoccio.axirassa.services.Service;
import com.zanoccio.axirassa.util.SigarLoader;

public class SentinelService implements Service {

	private static final String CPU_STAT_INSERT = "INSERT INTO SentinelCPUStats VALUES (?,?,?,?,?)";
	private static final String MEMORY_STAT_INSERT = "INSERT INTO SentinelMemoryStats VALUES (?,?,?,?)";
	private static final String DISKUSAGE_STAT_INSERT = "INSERT INTO SentinelDiskUsageStats VALUES (?,?,?,?,?)";
	private static final String NETWORK_STAT_INSERT = "INSERT INTO SentinelNetworkStats VALUES (?,?,?,?,?)";
	private static final String THROUGHPUT_STAT_INSERT = "INSERT INTO SentinelThroughputStats VALUES (?,?,?,?,?)";

	private final Session session;
	private final int machineid;

	private Sigar sigar;

	private ArrayList<CpuStat> cpustat;
	private MemoryStat memorystat;
	private ArrayList<DiskUsageStat> diskusagestat;
	private ArrayList<NetworkStat> networkstat;
	private ArrayList<ThroughputStat> throughputstat;


	public SentinelService(Session session, int machineid) {
		this.session = session;
		this.machineid = machineid;
	}


	@Override
	public void execute() throws Exception {
		retrieveStatistics();
		insertData();
	}


	public void retrieveStatistics() throws SigarException {
		SigarLoader.require();
		if (sigar == null)
			sigar = new Sigar();

		// CPU
		CpuPerc[] cpus = sigar.getCpuPercList();
		cpustat = new ArrayList<CpuStat>(cpus.length);
		int cpuid = 0;
		for (CpuPerc cpu : cpus) {
			CpuStat stat = new CpuStat();
			stat.cpuid = cpuid;
			stat.system = cpu.getSys();
			stat.user = cpu.getUser();

			cpustat.add(stat);

			cpuid++;
		}

		// MEMORY
		memorystat = new MemoryStat();
		Mem mem = sigar.getMem();
		memorystat.free = mem.getActualFree();
		memorystat.total = mem.getTotal();

		// DISK USAGE STAT

	}


	public void insertData() {
		Date date = new Date();
		System.out.println("Date: " + date);

		Transaction transaction = session.beginTransaction();
		if (cpustat != null) {
			SQLQuery query = session.createSQLQuery(CPU_STAT_INSERT);
			System.out.println(query.getQueryString());
			query.setInteger(0, machineid);
			query.setTimestamp(1, date);
			for (CpuStat stat : cpustat) {
				System.out.println("CPU: " + stat.cpuid);
				query.setInteger(2, stat.cpuid);

				System.out.println("user: " + stat.user);
				System.out.println("sys:  " + stat.system);
				query.setDouble(3, stat.user);
				query.setDouble(4, stat.system);
				query.executeUpdate();
			}
		}

		if (memorystat != null) {
			System.out.println("memory");
			SQLQuery query = session.createSQLQuery(MEMORY_STAT_INSERT);
			query.setInteger(0, machineid);
			query.setTimestamp(1, date);
			query.setLong(2, memorystat.free);
			query.setLong(3, memorystat.total);

			query.executeUpdate();
		}

		transaction.commit();
	}
}

class CpuStat {
	int cpuid;
	double user;
	double system;
}

class MemoryStat {
	long free;
	long total;
}

class DiskUsageStat {
	String disk;
	long free;
	long total;
}

class NetworkStat {
	String device;
	long send;
	long receive;
}

class ThroughputStat {
	String device;
	float send;
	float receive;
}