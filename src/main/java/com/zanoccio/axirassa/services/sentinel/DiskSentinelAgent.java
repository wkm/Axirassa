
package com.zanoccio.axirassa.services.sentinel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.SigarException;

public class DiskSentinelAgent extends AbstractSentinelStatisticsAgent {

	// TODO: verify these are 10^3 kilobytes, not 2^10 kilobytes.
	public final static int SCALING_FACTOR = 1000;

	private final ArrayList<SentinelStatistic> diskstat = new ArrayList<SentinelStatistic>();
	private final HashMap<String, DiskIOSnapshot> previoussnapshots = new HashMap<String, DiskIOSnapshot>();


	@Override
	public void execute() throws SigarException {
		diskstat.clear();

		FileSystem[] fslist = getSigar().getFileSystemList();

		// space for both throughput and usage
		diskstat.ensureCapacity(2 * fslist.length);

		for (FileSystem fs : fslist) {
			int type = fs.getType();

			// skip CDs, network stores, swap, etc.
			if (type != FileSystem.TYPE_LOCAL_DISK && type != FileSystem.TYPE_SWAP)
				continue;

			FileSystemUsage usage = getSigar().getFileSystemUsage(fs.getDirName());
			long used = SCALING_FACTOR * usage.getUsed();
			long total = SCALING_FACTOR * usage.getTotal();

			diskstat.add(new DiskUsageStatistic(getMachineID(), getDate(), fs.getDirName(), used, total));

			DiskIOSnapshot current = new DiskIOSnapshot();
			current.date = getDate();
			current.readbytes = used;
			current.writebytes = total;

			// compare with previous
			DiskIOSnapshot previous = previoussnapshots.get(fs.getDirName());
			if (previous != null) {
				long millis = getDate().getTime() - previous.date.getTime();
				long seconds = millis / 1000;

				double readrate = (current.readbytes - previous.readbytes) / seconds;
				double writerate = (current.writebytes - previous.writebytes) / seconds;

				diskstat.add(new DiskIOStatistic(getMachineID(), getDate(), fs.getDirName(), readrate, writerate));
			}

			previoussnapshots.put(fs.getDirName(), current);
		}
	}


	@Override
	public Collection<SentinelStatistic> getStatistics() {
		return diskstat;
	}

}

class DiskIOSnapshot {
	Date date;
	long readbytes;
	long writebytes;
}
