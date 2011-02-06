
package axirassa.services.sentinel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.SigarException;

import axirassa.services.sentinel.model.SentinelDiskIOStatisticEntity;
import axirassa.services.sentinel.model.SentinelDiskUsageStatisticEntity;
import axirassa.services.sentinel.model.SentinelStatisticEntity;

public class DiskSentinelAgent extends AbstractSentinelStatisticsAgent {

	// TODO: verify these are 10^3 kilobytes, not 2^10 kilobytes.
	public final static int SCALING_FACTOR = 1000;

	private final ArrayList<SentinelStatisticEntity> diskstat = new ArrayList<SentinelStatisticEntity>();
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

			SentinelDiskUsageStatisticEntity usagedatum = new SentinelDiskUsageStatisticEntity();

			usagedatum.machineid = getMachineID();
			usagedatum.date = getDate();
			usagedatum.disk = fs.getDirName();
			usagedatum.used = used;
			usagedatum.total = total;

			diskstat.add(usagedatum);

			DiskIOSnapshot current = new DiskIOSnapshot();
			current.date = getDate();
			current.readbytes = used;
			current.writebytes = total;

			// compare with previous
			DiskIOSnapshot previous = previoussnapshots.get(fs.getDirName());
			if (previous != null) {
				long millis = getDate().getTime() - previous.date.getTime();
				long seconds = millis / 1000;

				float readrate = (current.readbytes - previous.readbytes) / seconds;
				float writerate = (current.writebytes - previous.writebytes) / seconds;

				SentinelDiskIOStatisticEntity iodatum = new SentinelDiskIOStatisticEntity();

				iodatum.machineid = getMachineID();
				iodatum.date = getDate();
				iodatum.disk = fs.getDirName();
				iodatum.read = readrate;
				iodatum.write = writerate;

				diskstat.add(iodatum);
			}

			previoussnapshots.put(fs.getDirName(), current);
		}
	}


	@Override
	public Collection<SentinelStatisticEntity> getStatistics() {
		return diskstat;
	}

}

class DiskIOSnapshot {
	Date date;
	long readbytes;
	long writebytes;
}
