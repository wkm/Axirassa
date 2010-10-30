
package com.zanoccio.axirassa.services.sentinel;

import java.util.ArrayList;

import org.hibernate.Session;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.SigarException;

public class DiskUsageSentinelAgent extends AbstractSentinelStatisticsAgent {

	// TODO: verify these are 10^3 kilobytes, not 2^10 kilobytes.
	private final static int SCALING_FACTOR = 1000;
	private final ArrayList<DiskUsageStatistic> diskusagestat = new ArrayList<DiskUsageStatistic>();


	@Override
	public void execute() throws SigarException {
		diskusagestat.clear();

		FileSystem[] fslist = getSigar().getFileSystemList();
		diskusagestat.ensureCapacity(fslist.length);

		for (FileSystem fs : fslist) {
			int type = fs.getType();

			// skip CDs, network stores, swap, etc.
			if (type != FileSystem.TYPE_LOCAL_DISK && type != FileSystem.TYPE_SWAP)
				continue;

			FileSystemUsage usage = getSigar().getFileSystemUsage(fs.getDirName());
			long used = SCALING_FACTOR * usage.getUsed();
			long total = SCALING_FACTOR * usage.getTotal();

			diskusagestat.add(new DiskUsageStatistic(getMachineID(), getDate(), fs.getDirName(), used, total));
		}
	}


	@Override
	public void save(Session session) {
		for (DiskUsageStatistic stat : diskusagestat)
			stat.save(session);
	}

}
