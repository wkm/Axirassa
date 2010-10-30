
package com.zanoccio.axirassa.services.sentinel;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

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
		Mem mem = sigar.getMem();
		memorystat = new MemoryStatistic(machineid, date, mem.getActualUsed(), mem.getTotal());

		// DISK USAGE STAT
		diskusagestat = new ArrayList<DiskUsageStatistic>();
		FileSystem[] fslist = sigar.getFileSystemList();
		for (FileSystem fs : fslist) {
			int type = fs.getType();

			// skip CDs, network stores, swap, etc.
			if (type != FileSystem.TYPE_LOCAL_DISK && type != FileSystem.TYPE_SWAP)
				continue;

			// TODO: verify these are 10^3 kilobytes, not 2^10 kilobytes.
			FileSystemUsage usage = sigar.getFileSystemUsage(fs.getDirName());
			diskusagestat.add(new DiskUsageStatistic(machineid, date, fs.getDirName(), 1000 * usage.getUsed(),
			        1000 * usage.getTotal()));
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

		if (memorystat != null)
			memorystat.save(session);

		if (diskusagestat != null)
			for (DiskUsageStatistic stat : diskusagestat)
				stat.save(session);

		if (networkstat != null)
			for (NetworkStatistic stat : networkstat)
				stat.save(session);

		transaction.commit();
	}
}