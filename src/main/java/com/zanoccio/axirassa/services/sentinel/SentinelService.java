
package com.zanoccio.axirassa.services.sentinel;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import com.zanoccio.axirassa.services.Service;
import com.zanoccio.axirassa.util.SigarLoader;

public class SentinelService implements Service {

	private static final String CPU_STAT_INSERT = "INSERT INTO SentinelCPUStats VALUES (?,?,?,?,?)";
	private static final String MEMORY_STAT_INSERT = "INSERT INTO SentinelMemoryStats VALUES (?,?,?,?)";
	private static final String DISKUSAGE_STAT_INSERT = "INSERT INTO SentinelDiskUsageStats VALUES (?,?,?,?,?)";
	private static final String DISKIO_STAT_INSERT = "INSERT INTO SentinelDiskIOStats VALUES (?,?,?,?,?)";
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

	private long lastservice;

	private LinkedHashMap<String, DiskIOStat> diskiostat;


	public static void main(String[] param) throws SigarException {
		SentinelService service = new SentinelService(null, 0);
		System.out.println("   Started: " + service.toString());
		service.retrieveStatistics();
	}


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
		memorystat.used = mem.getActualUsed();
		memorystat.total = mem.getTotal();

		// DISK USAGE STAT
		diskusagestat = new ArrayList<DiskUsageStat>();
		FileSystem[] fslist = sigar.getFileSystemList();
		for (FileSystem fs : fslist) {
			int type = fs.getType();

			if (type != FileSystem.TYPE_LOCAL_DISK && type != FileSystem.TYPE_SWAP)
				// skip
				continue;

			DiskUsageStat usage_stat = new DiskUsageStat();
			usage_stat.disk = fs.getDirName();

			// TODO: verify these are 10^3 kilobytes, not 2^10 kilobytes.
			// retrieve usage
			FileSystemUsage usage = sigar.getFileSystemUsage(usage_stat.disk);
			usage_stat.used = 1000 * usage.getUsed();
			usage_stat.total = 1000 * usage.getTotal();

			DiskIOStat io_stat = new DiskIOStat();
			usage.getDiskWriteBytes();

			diskusagestat.add(usage_stat);
		}

		// NETWORK USAGE STAT
		networkstat = new ArrayList<NetworkStat>();
		String[] netinterfaces = sigar.getNetInterfaceList();
		for (String netinterface : netinterfaces) {
			NetInterfaceStat stat = sigar.getNetInterfaceStat(netinterface);
			NetworkStat netstat = new NetworkStat();

			netstat.device = sigar.getNetInterfaceConfig(netinterface).getDescription();
			netstat.receive = stat.getRxBytes();
			netstat.send = stat.getTxBytes();

			networkstat.add(netstat);
		}
	}


	public void insertData() {
		Date date = new Date();

		Transaction transaction = session.beginTransaction();
		if (cpustat != null) {
			SQLQuery query = session.createSQLQuery(CPU_STAT_INSERT);

			query.setInteger(0, machineid);
			query.setTimestamp(1, date);
			for (CpuStat stat : cpustat) {
				query.setInteger(2, stat.cpuid);

				query.setDouble(3, stat.user);
				query.setDouble(4, stat.system);
				query.executeUpdate();
			}
		}

		if (memorystat != null) {
			SQLQuery query = session.createSQLQuery(MEMORY_STAT_INSERT);
			query.setInteger(0, machineid);
			query.setTimestamp(1, date);
			query.setLong(2, memorystat.used);
			query.setLong(3, memorystat.total);

			query.executeUpdate();
		}

		if (diskusagestat != null) {
			SQLQuery query = session.createSQLQuery(DISKUSAGE_STAT_INSERT);
			query.setInteger(0, machineid);
			query.setTimestamp(1, date);

			for (DiskUsageStat stat : diskusagestat) {
				query.setString(2, stat.disk);
				query.setLong(3, stat.used);
				query.setLong(4, stat.total);

				query.executeUpdate();
			}
		}

		if (networkstat != null) {
			SQLQuery query = session.createSQLQuery(NETWORK_STAT_INSERT);
			query.setInteger(0, machineid);
			query.setTimestamp(1, date);

			for (NetworkStat stat : networkstat) {
				query.setString(2, stat.device);
				query.setLong(3, stat.send);
				query.setLong(4, stat.receive);
				query.executeUpdate();
			}
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
	long used;
	long total;
}

class DiskUsageStat {
	String disk;
	long used;
	long total;
}

class DiskIOStat {
	String disk;
	long read;
	long written;
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