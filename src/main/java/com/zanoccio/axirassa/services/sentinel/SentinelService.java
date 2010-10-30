
package com.zanoccio.axirassa.services.sentinel;

import java.util.ArrayList;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;
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

	private final ArrayList<Class<? extends SentinelAgent>> agentclasses = new ArrayList<Class<? extends SentinelAgent>>();
	private final ArrayList<SentinelAgent> agents = new ArrayList<SentinelAgent>();

	private ArrayList<CPUStatistic> cpustats;
	private MemoryStatistic memorystat;
	private ArrayList<DiskUsageStatistic> diskusagestat;
	private ArrayList<NetworkStatistic> networkstat;


	public static void main(String[] param) throws Exception {
		Session session = HibernateTools.getSession();
		SentinelService service = new SentinelService(session, 1);

		service.addAgent(CPUSentinelAgent.class);

		while (true) {
			service.execute();
			Thread.sleep(60 * 1000);
		}
	}


	public SentinelService(Session session, int machineid) {
		this.session = session;
		this.machineid = machineid;
	}


	public void addAgent(Class<? extends SentinelAgent> agentclass) {
		agentclasses.add(agentclass);
	}


	@Override
	public void execute() throws Exception {
		setupSigar();

		setupAgents();

		retrieveStatistics();
		insertData();
	}


	private void setupSigar() {
		SigarLoader.require();
		if (sigar == null)
			sigar = new Sigar();
	}


	private void setupAgents() {
		try {
			setupAgentsWithExceptions();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}


	private void setupAgentsWithExceptions() throws InstantiationException, IllegalAccessException {
		for (Class<? extends SentinelAgent> agentclass : agentclasses) {
			SentinelAgent agent = agentclass.newInstance();
			agent.setSigar(sigar);
			agents.add(agent);
		}
	}


	private void retrieveStatistics() throws SigarException {
		SigarLoader.require();
		if (sigar == null)
			sigar = new Sigar();

		Date date = new Date();

		for (SentinelAgent agent : agents) {
			agent.setMachineID(machineid);
			agent.setDate(date);
			agent.execute();
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


	private void insertData() {
		Date date = new Date();

		System.out.println("Inserting " + date);

		Transaction transaction = session.beginTransaction();
		for (SentinelAgent agent : agents)
			agent.save(session);

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