
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
import com.zanoccio.axirassa.util.HibernateTools;
import com.zanoccio.axirassa.util.SigarLoader;

public class SentinelService implements Service {

	private final Session session;
	private final int machineid;

	private Sigar sigar;

	private ArrayList<CPUStatistic> cpustats;
	private MemoryStatistic memorystat;
	private ArrayList<DiskUsageStatistic> diskusagestat;
	private ArrayList<NetworkStatistic> networkstat;
	private ArrayList<NetworkThroughputStatistic> throughputstat;

	private long lastservice;

	private LinkedHashMap<String, DiskIOStatistic> diskiostat;


	public static void main(String[] param) throws Exception {
		Session session = HibernateTools.getSession();
		Service service = new SentinelService(session, 1);

		while (true) {
			service.execute();
			Thread.sleep(60 * 1000);
		}
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

		Date date = new Date();

		// CPU
		CpuPerc[] cpus = sigar.getCpuPercList();
		cpustats = new ArrayList<CPUStatistic>(cpus.length);
		int cpuid = 0;
		for (CpuPerc cpu : cpus) {
			cpustats.add(new CPUStatistic(machineid, date, cpuid, cpu.getSys(), cpu.getUser()));

			cpuid++;
		}

		// MEMORY
		memorystat = new MemoryStatistic();
		Mem mem = sigar.getMem();
		memorystat.used = mem.getActualUsed();
		memorystat.total = mem.getTotal();

		// DISK USAGE STAT
		diskusagestat = new ArrayList<DiskUsageStatistic>();
		FileSystem[] fslist = sigar.getFileSystemList();
		for (FileSystem fs : fslist) {
			int type = fs.getType();

			if (type != FileSystem.TYPE_LOCAL_DISK && type != FileSystem.TYPE_SWAP)
				// skip
				continue;

			DiskUsageStatistic usage_stat = new DiskUsageStatistic();
			usage_stat.disk = fs.getDirName();

			// TODO: verify these are 10^3 kilobytes, not 2^10 kilobytes.
			// retrieve usage
			FileSystemUsage usage = sigar.getFileSystemUsage(usage_stat.disk);
			usage_stat.used = 1000 * usage.getUsed();
			usage_stat.total = 1000 * usage.getTotal();

			DiskIOStatistic io_stat = new DiskIOStatistic();
			usage.getDiskWriteBytes();

			diskusagestat.add(usage_stat);
		}

		// NETWORK USAGE STAT
		networkstat = new ArrayList<NetworkStatistic>();
		String[] netinterfaces = sigar.getNetInterfaceList();
		for (String netinterface : netinterfaces) {
			NetInterfaceStat stat = sigar.getNetInterfaceStat(netinterface);
			String interfacename = sigar.getNetInterfaceConfig(netinterface).getDescription();
			networkstat.add(new NetworkStatistic(machineid, date, interfacename, stat.getTxBytes(), stat.getRxBytes()));
		}
	}


	public void insertData() {
		Date date = new Date();

		System.out.println("Inserting " + date);

		Transaction transaction = session.beginTransaction();
		if (cpustats != null)
			for (CPUStatistic stat : cpustats)
				stat.save(session);

		if (memorystat != null) {
			SQLQuery query = session.createSQLQuery(MemoryStatistic.MEMORY_STAT_INSERT);
			query.setInteger(0, machineid);
			query.setTimestamp(1, date);
			query.setLong(2, memorystat.used);
			query.setLong(3, memorystat.total);

			query.executeUpdate();
		}

		if (diskusagestat != null) {
			SQLQuery query = session.createSQLQuery(DiskUsageStatistic.DISKUSAGE_STAT_INSERT);
			query.setInteger(0, machineid);
			query.setTimestamp(1, date);

			for (DiskUsageStatistic stat : diskusagestat) {
				query.setString(2, stat.disk);
				query.setLong(3, stat.used);
				query.setLong(4, stat.total);

				query.executeUpdate();
			}
		}

		if (networkstat != null)
			for (NetworkStatistic stat : networkstat)
				stat.save(session);

		transaction.commit();
	}
}